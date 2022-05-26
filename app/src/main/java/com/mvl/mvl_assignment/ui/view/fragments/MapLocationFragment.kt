package com.mvl.mvl_assignment.ui.view.fragments


import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.collection.ArrayMap
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.mvl.mvl_assignment.R
import com.mvl.mvl_assignment.databinding.FragmentMapLocationBinding
import com.mvl.mvl_assignment.helper.Constants
import com.mvl.mvl_assignment.helper.PrefDelegate
import com.mvl.mvl_assignment.helper.SharePrefStorage
import com.mvl.mvl_assignment.helper.showToast
import com.mvl.mvl_assignment.maphelper.locationhelper.interfaces.RetrofitMaps
import com.mvl.mvl_assignment.maphelper.map.ConnectivityReceiver
import com.mvl.mvl_assignment.maphelper.map.SupportMapFragmentWithScrollView
import com.mvl.mvl_assignment.maphelper.map.adapter.CitySelectionAdapter
import com.mvl.mvl_assignment.maphelper.map.model.AQIModel
import com.mvl.mvl_assignment.maphelper.map.model.CityModel
import com.mvl.mvl_assignment.maphelper.map.model.LocalityModel

import com.mvl.mvl_assignment.ui.view.interfaces.MapNavigator
import com.mvl.mvl_assignment.ui.viewmodel.MapFragmentViewModel
import com.mvl.mvl_assignment.base.BaseApplication
import com.mvl.mvl_assignment.ui.view.activity.MapActivity
import com.webcroptech.aladdin.view.map.adapter.LocalitySelectionAdapter
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class MapLocationFragment : Fragment(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    LocationListener, ConnectivityReceiver.ConnectivityReceiverListener, MapNavigator {

    private var year: Int = 0
    private var month: Int = 0
    private var client: GoogleApiClient? = null

    private var addressId: String = ""
    val REQUEST_LOCATION_CODE = 99
    var PROXIMITY_RADIUS = 10000
    private var user_id: Int = 0
    var mapFragment: SupportMapFragment? = null
    var mScrollView: ScrollView? = null
    var mContext: Context? = null
    private lateinit var prefs: SharedPreferences
    var authToken: String? = null
    var rlMapAddressContainer: RelativeLayout? = null
    var imvMapMarkerTemp: ImageView? = null
    var tieAddress: TextInputEditText? = null
    var tiePincode: TextInputEditText? = null
    var tieCity: TextInputEditText? = null
    var tieLocality: TextInputEditText? = null
    var btnProceed: AppCompatButton? = null
    var progressMap: ProgressBar? = null
    var tvHome: TextView? = null
    var tvOffice: TextView? = null
    var tvOthers: TextView? = null
    var back: ImageView? = null

    var bottomSheetDialogCitySelection: BottomSheetDialog? = null
    var bottomSheetDialogLocalitySelection: BottomSheetDialog? = null
    var customAutoCompleteTextView: AutoCompleteTextView? = null
    var isPlaces = false
    var isExisting = false
    var placesAddress: String? = null
    var innerLocalitySearch: EditText? = null
    var innerCitySearch: EditText? = null

    var address: String = ""
    var city: String = ""
    var state: String = ""
    var country: String = ""
    var pincode: String = ""
    var addressFetch: String = ""
    var locateProf: String = ""
    var nickName: String = ""


    //AutoCompleteTextView actvCitySearch;
    var rvLocalitySelection: RecyclerView? = null
    var rvCitySelection: RecyclerView? = null

    //TextView tvNoResults;
    var localitySelectionAdapter: LocalitySelectionAdapter? = null
    var citySelectionAdapter: CitySelectionAdapter? = null
    var selectedCity: CityModel? = null
    var selectedLocality: LocalityModel? = null

    private val provider: MapFragmentViewModel by viewModels()
    private lateinit var binding: FragmentMapLocationBinding


    private var param1: String? = null
    private var param2: String? = null

    var sharePrefStorage: SharePrefStorage = SharePrefStorage()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    // private val viewModel by viewModels<MapFragmentViewModel>(factoryProducer = factory { provider })


    lateinit var bind: FragmentMapLocationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        PrefDelegate.init(requireActivity())
        binding = FragmentMapLocationBinding.inflate(inflater)
        //retainInstance = true
        binding.lifecycleOwner = this
        binding.fragmentMapViewModel = provider
        MapFragmentViewModel.mainNavigator.mainNavigator = this
        mContext = requireActivity()
        initDeclaration(binding)


        // Manually checking internet connection
        checkConnection()
        if (mapFragment != null) {
            mapFragment!!.getMapAsync(this)
        } else {
            //CommonUtilities.showToast(this, Constants.ERROR_SOMETHING_WENT_WRONG)
            requireActivity().finish()
        }
        return binding.root
    }

    private fun initDeclaration(binding: FragmentMapLocationBinding) {
        arguments?.let {
            var args: String? = null
            if (requireArguments().getString("labelName") != null) {
                args = requireArguments().getString("labelName")
                if (args != null) {
                    if (args.equals("Location A", ignoreCase = true)) {
                        provider.valueALocation.value = sharePrefStorage.Loc_NickName_A
                    } else if (args.equals("Location B", ignoreCase = true)) {
                        provider.valueBLocation.value = sharePrefStorage.Loc_NickName_b
                    }
                }

                if ((!sharePrefStorage.Loc_NickName_A.isNullOrEmpty() && !sharePrefStorage.Loc_NickName_b.isNullOrEmpty()) && (
                            !sharePrefStorage.mapBLocation.isNullOrEmpty() && !sharePrefStorage.mapALocation.isNullOrEmpty()
                            )
                ) {
                    provider.btnTextChange.value = "Book"
                    provider.valueALocation.value = sharePrefStorage.Loc_NickName_A
                    provider.valueBLocation.value = sharePrefStorage.Loc_NickName_b
                } else if (provider.valueALocation.value.equals("Location A", ignoreCase = true)) {
                    provider.btnTextChange.value = "Set A"
                    if (!sharePrefStorage.Loc_NickName_A.isNullOrEmpty()) {
                        provider.valueALocation.value = sharePrefStorage.Loc_NickName_A
                    } else if (!sharePrefStorage.mapALocation.isNullOrEmpty())
                        provider.valueALocation.value = sharePrefStorage.mapALocation
                } else if (provider.valueBLocation.value.equals("Location B", ignoreCase = true)) {
                    if (!sharePrefStorage.Loc_NickName_b.isNullOrEmpty()) {
                        provider.btnTextChange.value = "Book"
                        provider.valueBLocation.value = sharePrefStorage.Loc_NickName_b
                    } else if (!sharePrefStorage.mapBLocation.isNullOrEmpty()) {
                        provider.btnTextChange.value = "Book"
                        provider.valueBLocation.value = sharePrefStorage.mapBLocation
                    } else {
                        provider.btnTextChange.value = "Set B"
                    }
                }
            } else if (requireArguments().getString("list") != null) {
                args = requireArguments().getString("list")
                if (args != null) {
                    //provider.onLocationSingleRequestAPI(args)

                    if (args.equals("getyearlistnavigation", ignoreCase = true)) {
                        provider.valueALocation.value = sharePrefStorage.mapALocation
                        provider.valueBLocation.value = sharePrefStorage.mapBLocation
                        addressFetch = sharePrefStorage.mapALocation!!

                        if (sharePrefStorage.mapALocation != null && sharePrefStorage.mapBLocation != null) {
                            provider.btnTextChange.value = "Book"
                        }
                        getAQIValue(
                            LatLng(
                                sharePrefStorage.latAValue!!.toDouble(),
                                sharePrefStorage.lngAValue!!.toDouble()
                            )
                        )
                    } else if (args.equals("Cachedlistnavigation", ignoreCase = true)) {
                        val locLabel = requireArguments().getString("location_label")
                        Log.v("loclabel",locLabel!!)

                        if (locLabel.equals("Location A", ignoreCase = true)) {
                            provider.valueALocation.value = sharePrefStorage.mapALocation
                            address = sharePrefStorage.mapALocation!!
                            addressFetch = sharePrefStorage.mapALocation!!
                            provider.btnTextChange.value="SetB"

                            getAQIValue(
                                LatLng(
                                    sharePrefStorage.latAValue!!.toDouble(),
                                    sharePrefStorage.lngAValue!!.toDouble()
                                )
                            )
                        } else if (locLabel.equals("Location B", ignoreCase = true)) {

                            if(provider.valueALocation.equals("Location A")){
                                provider.btnTextChange.value="Set A"
                            }else{
                                provider.btnTextChange.value="Book"
                            }
                            provider.valueBLocation.value = sharePrefStorage.mapBLocation
                            address = sharePrefStorage.mapBLocation!!
                            addressFetch = sharePrefStorage.mapBLocation!!
                            getAQIValue(
                                LatLng(
                                    sharePrefStorage.latBVAlue!!.toDouble(),
                                    sharePrefStorage.lngBValue!!.toDouble()
                                )
                            )
                        }

                    }
                }

            }

        }


        //prefs = PreferenceHelper.defaultPrefs(mContext!!)
        // authToken = prefs.getStringPref(AppConstant.SP_LOGIN_ACCESS_TOKEN, "")
        binding.root
        mScrollView = binding.root.findViewById(R.id.scroll_view_address)
        rlMapAddressContainer = binding.root.findViewById(R.id.rl_map_parent)
        imvMapMarkerTemp = binding.root.findViewById(R.id.imv_map_marker_temp)
        tieAddress = binding.root.findViewById(R.id.et_address)
        btnProceed = binding.root.findViewById(R.id.btn_proceed)
        progressMap = binding.root.findViewById(R.id.progress_map)

        back = binding.root.findViewById(R.id.iv_back)
        customAutoCompleteTextView = binding.root.findViewById(R.id.map_auto_complete_edit_text)
        mFusedLocationProviderClient = FusedLocationProviderClient(requireActivity())
        locationRequest = LocationRequest.create()
            .setInterval((30 * 1000).toLong())
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
        mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragmentWithScrollView?
        imvMapMarkerTemp!!.bringToFront()
        val apiKey = getString(R.string.google_maps_key_for_autocomplete_tv)
        if (!Places.isInitialized()) {
            Places.initialize(requireActivity(), apiKey)
        }

        if (requireActivity().intent.getStringExtra(Constants.ADDRESS) != null) {
            address = requireActivity().intent.getStringExtra(Constants.ADDRESS)!!
            city = requireActivity().intent.getStringExtra(Constants.CITY)!!
            state = requireActivity().intent.getStringExtra(Constants.STATE)!!
            country = requireActivity().intent.getStringExtra(Constants.COUNTRY)!!
            pincode = requireActivity().intent.getStringExtra(Constants.PINCODE)!!
            addressId = requireActivity().intent.getStringExtra(Constants.ADDRESS_ID)!!
            locateProf = requireActivity().intent.getStringExtra(Constants.PREF_LOCATION)!!
            nickName = requireActivity().intent.getStringExtra(Constants.NICK_NAME)!!

            // tieCity!!.setText(locateProf)
            tieLocality!!.setText(nickName)

            if (locateProf.equals("office", ignoreCase = true)) {
                // tvOffice!!.background = resources.getDrawable(R.drawable.rounded_yellow_edittext)
            }

            if (locateProf.equals("home", ignoreCase = true)) {
                // tvHome!!.background = resources.getDrawable(R.drawable.rounded_yellow_edittext)
            }

            if (locateProf.equals("others", ignoreCase = true)) {
                //tvOthers!!.background = resources.getDrawable(R.drawable.rounded_yellow_edittext)
            }
        }



        addressFetch = address


        val viewCitySelection: View =
            layoutInflater.inflate(R.layout.bottomsheetdialog_city_address, null)
        bottomSheetDialogCitySelection =
            BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogThemeNoFloating)
        bottomSheetDialogCitySelection!!.setContentView(viewCitySelection)
        bottomSheetDialogCitySelection!!.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event ->
            // getAction to make sure this doesn't double fire
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                //This is the filter
                if (event.action != KeyEvent.ACTION_DOWN) return@OnKeyListener true else {
                    bottomSheetDialogCitySelection!!.dismiss()
                    innerCitySearch!!.setText("")
                    //Hide your keyboard here!!!!!!
                    return@OnKeyListener true // pretend we've processed it
                }
            }
            false // Don't capture
        })

        bottomSheetDialogCitySelection!!.setOnShowListener {
            val mBehaviorCitySelection: BottomSheetBehavior<*> =
                BottomSheetBehavior.from(viewCitySelection.parent as View)
            mBehaviorCitySelection.peekHeight = 1000
            mBehaviorCitySelection.setBottomSheetCallback(object : BottomSheetCallback() {
                override fun onStateChanged(view: View, newState: Int) {
                    //showing the different states
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN ->                                 //if you want the modal to be dismissed when user drags the bottomsheet down
                            innerCitySearch!!.setText("")
                        BottomSheetBehavior.STATE_EXPANDED -> innerCitySearch!!.setText("")
                        BottomSheetBehavior.STATE_COLLAPSED -> innerCitySearch!!.setText("")
                        BottomSheetBehavior.STATE_DRAGGING -> {
                        }
                        BottomSheetBehavior.STATE_SETTLING -> {
                        }
                    }
                }

                override fun onSlide(view: View, slideOffset: Float) {
                    /*                        if (mBehaviorLocalitySelection.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                                                if (Math.abs(slideOffset) > 0) {
                                                    mBehaviorLocalitySelection.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                                    mBehaviorLocalitySelection.setState(BottomSheetBehavior.STATE_HIDDEN);
                                                }
                                            }*/
                }
            })
        }


        rvCitySelection = viewCitySelection.findViewById(R.id.rv_city_selection)
        innerCitySearch = viewCitySelection.findViewById(R.id.et_city_search)
        rvCitySelection!!.setLayoutManager(LinearLayoutManager(requireActivity()))
        val viewLocalitySelection: View =
            layoutInflater.inflate(R.layout.bottomsheetdialog_locality_address, null)
        bottomSheetDialogLocalitySelection =
            BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogThemeNoFloating)
        bottomSheetDialogLocalitySelection!!.setContentView(viewLocalitySelection)
        bottomSheetDialogLocalitySelection!!.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event ->
            // getAction to make sure this doesn't double fire
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                //This is the filter
                if (event.action != KeyEvent.ACTION_DOWN) return@OnKeyListener true else {
                    bottomSheetDialogLocalitySelection!!.dismiss()
                    innerLocalitySearch!!.setText("")
                    //Hide your keyboard here!!!!!!
                    return@OnKeyListener true // pretend we've processed it
                }
            }
            false // Don't capture
        })
        bottomSheetDialogLocalitySelection!!.setOnShowListener {
            val mBehaviorLocalitySelection: BottomSheetBehavior<*> =
                BottomSheetBehavior.from(viewLocalitySelection.parent as View)
            mBehaviorLocalitySelection.peekHeight = 1000
            mBehaviorLocalitySelection.setBottomSheetCallback(object : BottomSheetCallback() {
                override fun onStateChanged(view: View, newState: Int) {
                    //showing the different states
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN ->                                 //if you want the modal to be dismissed when user drags the bottomsheet down
                            innerLocalitySearch!!.setText("")
                        BottomSheetBehavior.STATE_EXPANDED -> innerLocalitySearch!!.setText("")
                        BottomSheetBehavior.STATE_COLLAPSED -> innerLocalitySearch!!.setText("")
                        BottomSheetBehavior.STATE_DRAGGING -> {
                        }
                        BottomSheetBehavior.STATE_SETTLING -> {
                        }
                    }
                }

                override fun onSlide(view: View, slideOffset: Float) {
                    /*                        if (mBehaviorLocalitySelection.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                                                if (Math.abs(slideOffset) > 0) {
                                                    mBehaviorLocalitySelection.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                                    mBehaviorLocalitySelection.setState(BottomSheetBehavior.STATE_HIDDEN);
                                                }
                                            }*/
                }
            })
        }

        rvLocalitySelection = viewLocalitySelection.findViewById(R.id.rv_locality_selction)
        innerLocalitySearch = viewLocalitySelection.findViewById(R.id.et_locality_search)
        rvLocalitySelection!!.setLayoutManager(LinearLayoutManager(requireActivity()))


        //adding a TextChangedListener
        //to call a method whenever there is some change on the EditText
        innerLocalitySearch!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                //after the change calling the method and passing the search input
                localitySelectionAdapter!!.getFilter().filter(editable.toString())
            }
        })
        innerCitySearch!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                //after the change calling the method and passing the search input
                citySelectionAdapter!!.getFilter().filter(editable.toString())
            }
        })



        back!!.setOnClickListener {
            requireActivity().finish()
        }

    }


    private fun getAddressAPI(
        latitude: Double,
        longitude: Double
    ): Address? {
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(requireActivity(), Locale.getDefault())
        try {
            addresses = geocoder.getFromLocation(
                latitude,
                longitude,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses[0]
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


    private fun getAddressLat(latitude: Double, longitude: Double) {
        val locationAddress: Address = getAddressAPI(latitude, longitude)!!
        if (locationAddress != null) {
            val address = locationAddress.getAddressLine(0)
            val address1 = locationAddress.getAddressLine(1)
            val city = locationAddress.locality
            val state = locationAddress.adminArea
            val country = locationAddress.countryName
            val postalCode = locationAddress.postalCode
            var currentLocation: String?
            if (!TextUtils.isEmpty(address)) {
                currentLocation = address
                if (!TextUtils.isEmpty(address1)) currentLocation += """
     
     $address1
     """.trimIndent()
                if (!TextUtils.isEmpty(city)) {
                    currentLocation += """
                        
                        $city
                        """.trimIndent()
                    if (!TextUtils.isEmpty(postalCode)) currentLocation += " - $postalCode"
                } else {
                    if (!TextUtils.isEmpty(postalCode)) currentLocation += """
     
     $postalCode
     """.trimIndent()
                }
                if (!TextUtils.isEmpty(state)) currentLocation += """
     
     $state
     """.trimIndent()
                if (!TextUtils.isEmpty(country)) currentLocation += """
     
     $country
     """.trimIndent()
                // tvEmpty.setVisibility(View.GONE)
                // tvAddress.setText(currentLocation)
                // tvAddress.setVisibility(View.VISIBLE)
                if (!btnProceed!!.isEnabled) btnProceed!!.isEnabled = true

                /* CommonUtilities.getInstance()?.showProgressDialog(
                     this, this!!.resources.getString(
                         R.string.progressmsg
                     )
                 )*/
                if (addressFetch.equals("")) {
                    mapAddApplyApi(address, city, state, country, postalCode, latitude, longitude)
                } else {
                    mapUpdateApplyApi(
                        address,
                        city,
                        state,
                        country,
                        postalCode,
                        latitude,
                        longitude
                    )
                }
            }
        }
    }

    /**
     * Fetch the address
     */
    fun getAPILocationFromAddress(context: Context?, strAddress: String?): LatLng? {
        val coder = Geocoder(context)
        val address: List<Address>
        var p1: LatLng? = null
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            val location: Address = address[0]
            p1 = LatLng(location.getLatitude(), location.getLongitude())
            getAddressLat(p1.latitude, p1.longitude)
            //retrieveData(p1.latitude,p1.longitude)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return p1
    }


    fun mapUpdateApplyApi(
        address: String,
        city: String,
        state: String,
        country: String,
        pincode: String,
        latitude: Double,
        longitude: Double
    ) {
        try {
            val mapParmsValues: ArrayMap<String, String> = ArrayMap<String, String>()
            mapParmsValues.put("address", address)
            mapParmsValues.put("city", city)
            mapParmsValues.put("state", state)
            mapParmsValues.put("postcode", pincode)
            mapParmsValues.put("country", country)
            mapParmsValues.put("user_id", user_id.toString())
            mapParmsValues.put("nic_name", "raji")
            mapParmsValues.put("add_id", addressId)
            mapParmsValues.put("pref_location", locateProf)
            mapParmsValues.put("lat_value", latitude.toString())
            mapParmsValues.put("log_value", longitude.toString())
            Log.v("params", mapParmsValues.toString())
            /* val retroApiCall =
                 RetrofitConnect.getInstance()?.getRetrofitApiService(ApiUtils.BASE_URL)
             val postRes: Call<MapAddResponse?> =
                 retroApiCall?.addMapAddressApi(Request.API_ADDRESS_UPDATE, mapParmsValues)!!
             RetrofitConnect.getInstance()
                 ?.addRetrofitCalls(postRes, this, Request.API_ADDRESS_UPDATE)*/

        } catch (e: Exception) {
            // ExceptionTrack.getInstance().TrackLog(e)
        }
    }

    fun mapAddApplyApi(
        address: String,
        city: String,
        state: String,
        country: String,
        pincode: String,
        latitude: Double,
        longitude: Double
    ) {
        try {
            val mapParmsValues: ArrayMap<String, String> = ArrayMap<String, String>()
            mapParmsValues.put("address", address)
            mapParmsValues.put("city", city)
            mapParmsValues.put("state", state)
            mapParmsValues.put("postcode", pincode)
            mapParmsValues.put("country", country)
            mapParmsValues.put("user_id", user_id.toString())
            mapParmsValues.put("nic_name", "raji")
            mapParmsValues.put("lat_value", latitude.toString())
            mapParmsValues.put("log_value", longitude.toString())
            mapParmsValues.put("pref_location", locateProf)
            Log.v("params", mapParmsValues.toString())

        } catch (e: Exception) {
            // ExceptionTrack.getInstance().TrackLog(e)
        }
    }


    //Map
    var mMap: GoogleMap? = null
    var markerClickedLocation: MarkerOptions? = null
    var markerAvailable = false
    var locationRequest: LocationRequest? = null
    var mFusedLocationProviderClient: FusedLocationProviderClient? = null

    @Synchronized
    protected fun bulidGoogleApiClient() {
        client = GoogleApiClient.Builder(requireActivity())
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        client!!.connect()
    }

    var currentLocationMarker: Marker? = null
    override fun onLocationChanged(location: Location) {

        val latLng = LatLng(location.latitude, location.longitude)

        val centreOfSouthKorea = latLng
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Current Location")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        markerAvailable = true
        markerClickedLocation = getMapMarker(centreOfSouthKorea)
        currentLocationMarker = mMap!!.addMarker(markerOptions)

        mMap!!.addMarker(markerClickedLocation!!)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(centreOfSouthKorea, 5f))

        setMapMarkerAndAnimate(centreOfSouthKorea, 15f, true)
        setMapMarkerAndAnimateWithoutGeoCoder(centreOfSouthKorea, 15f, true)

        DownloadRawData().execute(centreOfSouthKorea)

        if (client != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client!!, this)
        }


    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (addressFetch != "") {
            getLocationFromAddress(requireActivity(), addressFetch)
        } else {
            bulidGoogleApiClient()

/*  val centreOfSouthIndia = LatLng(13.905586, 77.294968)
 markerAvailable = true
 markerClickedLocation = getMapMarker(centreOfSouthIndia)
 mMap!!.addMarker(markerClickedLocation)
 mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(centreOfSouthIndia, 5f))
 DownloadRawData().execute(centreOfSouthIndia )*/
        }
        mMap!!.mapType = MAP_TYPE_NORMAL
        mMap!!.uiSettings.isZoomControlsEnabled = true
        mMap!!.uiSettings.isMyLocationButtonEnabled
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mMap!!.isMyLocationEnabled = true
        mMap!!.uiSettings.isMyLocationButtonEnabled = true
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragmentWithScrollView?)!!.setListener(
            object :
                SupportMapFragmentWithScrollView.OnTouchListener {
                override fun onTouch() {
                    mScrollView!!.requestDisallowInterceptTouchEvent(true)
                }
            })
        markerAvailable = false

/*  AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
     getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);*/
//((EditText)autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input).setTextSize(10.0f);
        customAutoCompleteTextView!!.setOnClickListener { // Set the fields to specify which types of place data to return.
            val fields: List<Place.Field> = Arrays.asList<Place.Field>(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS,
                Place.Field.ADDRESS_COMPONENTS
            )
            // Start the autocomplete intent.
            val intent: Intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            ).setCountry("KR")
                .build(requireActivity())
            startActivityForResult(intent, Constants.REQUEST_CODE_AUTOCOMPLETE_MAP_ACTIVITY)
        }


/*
autocompleteFragment.setCountry("IN");
autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS ));
autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
 @Override
 public void onPlaceSelected(@NotNull Place place) {
     // TODO: Get info about the selected place.
     AppUtils.showVLog("onPlaceSelected", "Place: " + place.getName() + ", " + place.getId()+ ", " +place.getAddress()+ place.getAddressComponents().asList());
     setMapMarkerAndAnimate(place.getLatLng(), 15, true);


     //test
     */
/*if (place.getAddressComponents() != null) {
         List<AddressComponent> addressComponents = place.getAddressComponents().asList();
         for (int i = 0; i < addressComponents.size(); i++) {
             if (addressComponents.get(i).getTypes().get(0).equals("country")) {
                 countryCode = addressComponents.get(i).getShortName();
                 break;
             }
         }
     }*/
/*
 }

 @Override
 public void onError(@NotNull Status status) {
     // TODO: Handle the error.
     AppUtils.showVLog("onError", "An error occurred: " + status);
 }
});*/

        mMap!!.setOnMapClickListener(OnMapClickListener { latLng ->
            if (markerClickedLocation != null && markerAvailable) {
                return@OnMapClickListener
            }
            markerAvailable = true
            markerClickedLocation = getMapMarker(latLng)
            mMap!!.addMarker(markerClickedLocation!!)
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f))
            //loadPlaceDetailsApiByLatLong(latLng);
            //loadPlaceDetailsGeocoderByLatLong(latLng);
            DownloadRawData().execute(latLng)
        })
        mMap!!.setOnCameraIdleListener {
            if (markerAvailable) {
                imvMapMarkerTemp!!.visibility = View.GONE
                if (!isExisting) {
                    if (!isPlaces) setMapMarkerAndAnimate(
                        mMap!!.cameraPosition.target,
                        15f,
                        false
                    ) else setMapMarkerAndAnimateWithoutGeoCoder(
                        mMap!!.cameraPosition.target, 15f, false
                    )
                    isPlaces = false
                }
                isExisting = false
            }
        }
        mMap!!.setOnCameraMoveStartedListener {
            mMap!!.clear()
            if (markerAvailable) {
                imvMapMarkerTemp!!.visibility = View.VISIBLE
            }
        }
    }

    fun gpsLocationInitialize() {
/* if (!AccountActivity.isLocationPermissionsGranted(this)) {
  return
}*/
        try {
            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!)
            builder.addLocationRequest(locationRequest!!)
            val settingsClient = LocationServices.getSettingsClient(requireActivity())
            settingsClient.checkLocationSettings(builder.build())
                .addOnSuccessListener { //GPS switched ON.
                    deviceLocationAndSetMapMarker()
                }
                .addOnFailureListener { e ->
                    val statusCode = (e as ApiException).statusCode
                    when (statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(
                                requireActivity(),
                                Constants.REQUEST_PERMISSIONS_LOCATION_SETTINGS_REQUEST_CODE
                            )
                        } catch (sie: SendIntentException) {
                            sie.printStackTrace()
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        }
                        LocationSettingsStatusCodes.CANCELED -> {
                        }
                    }
                }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
// Check for the integer request code originally supplied to startResolutionForResult().
        if (requestCode == Constants.REQUEST_PERMISSIONS_LOCATION_SETTINGS_REQUEST_CODE) {
            when (resultCode) {
                AppCompatActivity.RESULT_OK -> deviceLocationAndSetMapMarker()
                AppCompatActivity.RESULT_CANCELED -> {
                }
            }
        } else if (requestCode == Constants.REQUEST_CODE_AUTOCOMPLETE_MAP_ACTIVITY) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val place: Place = Autocomplete.getPlaceFromIntent(data!!)
                /* CommonUtilities.showVLog(
                     "onPlaceSelected",
                     "Place: " + place.getName().toString() + ", " + place.getId()
                         .toString() + ", " + place.getAddress() + place.getAddressComponents()
                     !!.asList()
                 )*/
                var address1: String = place.getName().toString() + ", "
                for (i in 0 until place.getAddressComponents()!!.asList().size) {
                    val longName: String = place.getAddressComponents()!!.asList().get(i).getName()
                    if (place.getAddressComponents()!!.asList().get(i).getTypes()
                            .contains("street_number")
                    ) {
                        address1 = "$address1$longName, "
                    } else if (place.getAddressComponents()!!.asList().get(i).getTypes()
                            .contains("route")
                    ) {
                        address1 = "$address1$longName, "
                    } else if (place.getAddressComponents()!!.asList().get(i).getTypes()
                            .contains("sublocality")
                    ) {
                        address1 = "$address1$longName, "
                    }
                }
                placesAddress = address1
                isPlaces = true
                setMapMarkerAndAnimate(place.getLatLng()!!, 15f, true)

                // tieCity!!.setText(place.name.toString())
                tieAddress!!.setText(placesAddress)

                provider.locationAddress.value = placesAddress


                // do query with address
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
                Toast.makeText(
                    requireActivity(),
                    "Error: " + status.statusMessage,
                    Toast.LENGTH_LONG
                ).show()
                Log.i("Map", status.statusMessage!!)
            } else if (resultCode == AppCompatActivity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    // Set the map's camera position to the current location of the device.
    private fun deviceLocationAndSetMapMarker(): Unit {
        try {

            val locationResult: Task<*> =
                mFusedLocationProviderClient!!.lastLocation

            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Set the map's camera position to the current location of the device.
                    val mLastKnownLocation =
                        task.result as Location
                    if (mLastKnownLocation != null && mMap != null) {
                        setMapMarkerAndAnimate(
                            LatLng(
                                mLastKnownLocation.latitude, mLastKnownLocation.longitude
                            ), 15f, true
                        )
                    }
                    if (mMap != null) {
                        mMap!!.isMyLocationEnabled = true
                        mMap!!.setOnMyLocationButtonClickListener {
                            if (mLastKnownLocation == null) {
                                deviceLocationAndSetMapMarker()
                            } else {
                                setMapMarkerAndAnimate(
                                    LatLng(
                                        mLastKnownLocation.latitude, mLastKnownLocation.longitude
                                    ), 15f, true
                                )
                            }
                            false
                        }
                    }

                }
            }


        } catch (e: SecurityException) {
            //AppUtils.showVLog("getDeviceLocation", "Exception: %s" + e.message)
        }
    }


    /**
     * Fetch the address
     */
    fun getLocationFromAddress(context: Context?, strAddress: String?): LatLng? {
        val coder = Geocoder(context)
        val address: List<Address>
        var p1: LatLng? = null
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            val location: Address = address[0]
            p1 = LatLng(location.getLatitude(), location.getLongitude())

            DownloadRawData().execute(p1)
            setMapMarkerAndAnimate(p1, 15f, true)
            setMapMarkerAndAnimateWithoutGeoCoder(p1, 15f, true)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return p1
    }


    private fun setMapMarkerAndAnimate(latLng: LatLng, cameraZoom: Float, animate: Boolean) {
        if (markerClickedLocation != null) {
            mMap!!.clear()
        }
        markerAvailable = true
        markerClickedLocation = getMapMarker(latLng)
        mMap!!.addMarker(markerClickedLocation!!)
        if (animate) {
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, cameraZoom))
        }
// loadPlaceDetailsApiByLatLong(latLng);
//loadPlaceDetailsGeocoderByLatLong(latLng);


        DownloadRawData().execute(latLng)
    }

    private fun setMapMarkerAndAnimateWithoutGeoCoder(
        latLng: LatLng, cameraZoom: Float,
        animate: Boolean
    ) {
        if (markerClickedLocation != null) {
            mMap!!.clear()
        }
        markerAvailable = true
        markerClickedLocation = getMapMarker(latLng)
        mMap!!.addMarker(markerClickedLocation!!)
        if (animate) {
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, cameraZoom))
        }
//loadPlaceDetailsApiByLatLong(latLng);
//loadPlaceDetailsGeocoderByLatLong(latLng);
    }

    fun getMapMarker(latLng: LatLng?): MarkerOptions {
        return MarkerOptions()
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker)).position(latLng!!)
    }

    var selectedLatitude = 0.0
    var selectedLongitude = 0.0


    /**
     * Get the address by lat lang
     */
    fun getAddress(lat: Double, lng: Double): Unit {
        val geocoder = Geocoder(mContext, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (addresses.size > 0) {
                val obj = addresses[0]
                var add = obj.getAddressLine(0)
                add = """
     $add
     ${obj.countryName}
     """.trimIndent()
                add = """
     $add
     ${obj.countryCode}
     """.trimIndent()
                add = """
     $add
     ${obj.adminArea}
     """.trimIndent()
                add = """
     $add
     ${obj.postalCode}
     """.trimIndent()
                add = """
     $add
     ${obj.subAdminArea}
     """.trimIndent()
                add = """
     $add
     ${obj.locality}
     """.trimIndent()
                add = """
     $add
     ${obj.subThoroughfare}
     """.trimIndent()
                Log.v("IGA", "Address$add")

                requireActivity().runOnUiThread {


                    tieAddress!!.setText(obj.getAddressLine(0))

                    if (provider.btnTextChange.value.equals("Set A")) {
                        sharePrefStorage.latAValue = lat.toString()
                        sharePrefStorage.lngAValue = lng.toString()
                    } else if (provider.btnTextChange.value.equals("Set B")) {
                        sharePrefStorage.latBVAlue = lat.toString()
                        sharePrefStorage.lngBValue = lng.toString()
                    }
                    provider.locationAddress.value = obj.getAddressLine(0)
                    getAQIValue(LatLng(lat, lng))

                }
            }
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()

            requireActivity().runOnUiThread {
                Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun resetAddressFields() {
        tieAddress!!.setText("")
        tiePincode!!.setText("")
        tieLocality!!.setText("")
        tieCity!!.setText("")
        selectedCity = null
        selectedLocality = null
    }

    fun animateCameraDefaultMapLocation() {
        if (mMap != null) {
            val centreOfSouthIndia = LatLng(13.905586, 77.294968)
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(centreOfSouthIndia, 5f))
        }
    }

    private inner class DownloadRawData :
        AsyncTask<LatLng?, Void?, ArrayList<String>>() {
        var progressDialog: ProgressDialog? = null
        var address1: String? = null
        var address2: String? = null
        var city: String? = null
        var state: String? = null
        var country: String? = null
        var county: String? = null
        var pIN: String? = null

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(requireActivity())
            progressDialog!!.setMessage("Fetching location")
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        }

        protected override fun doInBackground(vararg latLng: LatLng?): ArrayList<String>? {

            getAddress(latLng[0]!!.latitude, latLng[0]!!.longitude)
            return retrieveData(latLng[0]!!.latitude, latLng[0]!!.longitude)
        }

        override fun onPostExecute(s: ArrayList<String>) {
            super.onPostExecute(s)
            if (progressDialog != null) progressDialog!!.dismiss()
            /*            LocationInfoDialog locationinfoDialog=new LocationInfoDialog(getActivity(),s);
            locationinfoDialog.show();
            locationinfoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            locationinfoDialog.setCancelable(false);*/
            //tieAddress.setText(s.get(0));

        }

        fun init() {
            address1 = ""
            address2 = ""
            city = ""
            state = ""
            country = ""
            county = ""
            pIN = ""
        }

        @Throws(UnsupportedEncodingException::class)
        private fun createUrl(latitude: Double, longitude: Double): String {
            init()
            return "https://maps.googleapis.com/maps/api/geocode/json?" + "latlng=" + latitude + "," + longitude + "&key=" + resources.getString(
                R.string.google_maps_key_for_place_search
            )
        }

        private fun buildUrl(latitude: Double, longitude: Double): URL? {
            try {
                Log.w("MapAct", "buildUrl: " + createUrl(latitude, longitude))
                return URL(createUrl(latitude, longitude))
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                Log.e("MapAct", "can't construct location object")
                return null
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            return null
        }

        @Throws(IOException::class)
        private fun getResponseFromHttpUrl(url: URL?): String? {
            val urlConnection = url!!.openConnection() as HttpURLConnection
            return try {
                val input = urlConnection.inputStream
                val scanner = Scanner(input)
                scanner.useDelimiter("\\A")
                if (scanner.hasNext()) {
                    scanner.next()
                } else {
                    null
                }
            } finally {
                urlConnection.disconnect()
            }
        }

        private fun retrieveData(latitude: Double, longitude: Double): ArrayList<String> {
            val strings = ArrayList<String>()
            try {
                val responseFromHttpUrl = getResponseFromHttpUrl(buildUrl(latitude, longitude))
                val jsonResponse = JSONObject(responseFromHttpUrl)
                val status = jsonResponse.getString("status")
                if (status.equals("OK", ignoreCase = true)) {
                    val results = jsonResponse.getJSONArray("results")
                    val zero = results.getJSONObject(0)
                    selectedLatitude = latitude
                    selectedLongitude = longitude


                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return strings
        }
    }


    private fun getAQIValue(latLng: LatLng) {
        val url = "https://api.waqi.info/"

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create<RetrofitMaps>(RetrofitMaps::class.java!!)

        val lat = String.format("%.1f", latLng.latitude)
        val lng = String.format("%.1f", latLng.longitude)

        val call = service.getAqiValue(
            lat.toDouble(), lng.toDouble()
        )

        call.enqueue(object : Callback<AQIModel> {
            override fun onResponse(
                call: Call<AQIModel>,
                response: Response<AQIModel>
            ) {

                try {
                    try {
                        if (response.body()!! != null && response.body()!!.status.equals(
                                "ok",
                                ignoreCase = true
                            )
                        ) {

                            var aqi = response.body()!!.data.aqi

                            if (provider.btnTextChange.value.equals("Set A")) {
                                sharePrefStorage.mapQAValue = response.body()!!.data.aqi.toString()

                            } else if (provider.btnTextChange.value.equals("Set B")) {
                                sharePrefStorage.mapQBValue = response.body()!!.data.aqi.toString()
                            }
                            Log.e("lqv", response.body()!!.data.aqi.toString())
                            provider.airQualityValue.value =
                                "AQV:" + " " + response.body()!!.data.aqi.toString()

                            // marker.tag = i
                        } else {
                            // marker.tag = response.body()!!.results.get(i).id
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    // }

                } catch (e: Exception) {
                    Log.d("onResponse", "There is an error")
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<AQIModel>, t: Throwable) {
                Log.d("onFailure", t.toString())

            }
        })

    }


    override fun onResume() {
        super.onResume()
// register connection status listener
        if (Build.VERSION.SDK_INT < 24) {
            BaseApplication.instance!!.setConnectivityListener(this)
        }
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showAlert(isConnected)
    }

    // Showing the  interent connection retry popup
    private fun showAlert(isConnected: Boolean) {
        if (isConnected) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission()
            }
        } else {
            val alertDialog = AlertDialog.Builder(requireActivity())
                .setMessage("Please switch ON your internet connection & try again")
                .setCancelable(false)
                .setPositiveButton(
                    "Retry"
                ) { dialogInterface, i -> checkConnection() }
                .create()
            alertDialog.show()
        }
    }

    // Method to manually check connection status
    private fun checkConnection() {
        val isConnected: Boolean = ConnectivityReceiver.isConnected()
        showAlert(isConnected)
    }

    companion object {
        fun start(context: Context?, accountActivity: Activity) {
            accountActivity.startActivityForResult(
                Intent(context, MapActivity::class.java),
                Constants.REQUEST_CODE_TO_START_MAP_ADDRESS_ACTIVITY
            )
        }

        private const val TRIGGER_AUTO_COMPLETE = 100
        private const val AUTO_COMPLETE_DELAY: Long = 300
    }


    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onConnected(p0: Bundle?) {
        locationRequest = LocationRequest()
        locationRequest!!.interval = 100
        locationRequest!!.fastestInterval = 1000
        locationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                client!!,
                locationRequest!!,
                this
            )
        }
    }

    override fun onConnectionSuspended(p0: Int) {

    }


    private fun checkLocationPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_CODE
                )
            }
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    if (client == null) {
                        bulidGoogleApiClient()
                    }
                    mMap!!.isMyLocationEnabled = true
                }
            } else {
                Toast.makeText(requireActivity(), "Permission Denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onGetAddress() {
        TODO("Not yet implemented")
    }

    override fun onGetAddressSingle(id: String, latitude: Double, longitude: Double) {
        addressFetch = id
        val latLng = LatLng(latitude, longitude)
        getAQIValue(latLng)
        getLocationFromAddress(requireActivity(), id)
    }

    override fun onGetAirQualityValue() {
        TODO("Not yet implemented")
    }

    override fun navigation(locationLabel: String) {
        if (locationLabel.equals(
                "Location value A",
                ignoreCase = true
            ) && !provider.valueALocation.value.equals("Location A", ignoreCase = true)
        )
            moveNAvigationToSecondPage("Location A")
        else if (locationLabel.equals(
                "Location value B",
                ignoreCase = true
            ) && !provider.valueBLocation.value.equals("Location B", ignoreCase = true)
        ) {
            moveNAvigationToSecondPage("Location B")
        } else {
            if(locationLabel.equals("Location value A", ignoreCase = true)) {
                moveNavigationtoCachedPage("Loocation A")
            }else if(locationLabel.equals("Location value B", ignoreCase = true)){
                moveNavigationtoCachedPage("Loocation B")
            }
        }
    }

    private fun moveNavigationtoCachedPage(location: String) {
        findNavController().navigate(
            MapLocationFragmentDirections.actionBookListFragment(
                "cachedListFragment",location
            )
        )
    }

    override fun LocationAddOnClick() {
        val calendar = Calendar.getInstance()
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DATE]

        provider.onLocationRequestAPI(year.toString(), month = month.toString())
    }

    override fun onerrorMsg(errorMsg: String) {
        requireActivity().showToast(errorMsg)

    }

    override fun verifyLocationSuccess(Status: String, id: String) {


        findNavController().navigate(
            MapLocationFragmentDirections.actionMapFragmentToBookFragment(
                id
            )
        )


    }


    //Navigation
    fun moveNAvigationToSecondPage(labelName: String) {
        findNavController().navigate(MapLocationFragmentDirections.actionNickNameFragment(labelName))
    }


}




