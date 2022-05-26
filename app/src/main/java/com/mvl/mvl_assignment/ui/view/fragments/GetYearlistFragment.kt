package com.mvl.mvl_assignment.ui.view.fragments

import RecyclerViewAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mvl.mvl_assignment.BuildConfig
import com.mvl.mvl_assignment.adapter.CachedAdapter
import com.mvl.mvl_assignment.data.model.response.GetAddress
import com.mvl.mvl_assignment.data.model.response.GetListAddress
import com.mvl.mvl_assignment.databinding.FragmentGetYearlistBinding
import com.mvl.mvl_assignment.helper.PrefDelegate
import com.mvl.mvl_assignment.helper.SharePrefStorage
import com.mvl.mvl_assignment.helper.showToast
import com.mvl.mvl_assignment.maphelper.locationhelper.interfaces.RetrofitMaps
import com.mvl.mvl_assignment.model.CashedList
import com.mvl.mvl_assignment.ui.view.interfaces.BookNavigator
import com.mvl.mvl_assignment.ui.viewmodel.GetYearListFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


@AndroidEntryPoint
class GetYearlistFragment : Fragment(), BookNavigator {

    private var param1: String? = null
    private var param2: String? = null
    var locLabel: String? = null

    private val viewModel: GetYearListFragmentViewModel by viewModels()
    private lateinit var binding: FragmentGetYearlistBinding
    var mContext: Context? = null
    private val items = MutableLiveData<ArrayList<GetAddress>>()
    private val cachedLocation = MutableLiveData<ArrayList<CashedList>>()
    var sharePrefStorage: SharePrefStorage = SharePrefStorage()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        PrefDelegate.init(requireActivity())
        binding = FragmentGetYearlistBinding.inflate(inflater)
        //retainInstance = true
        binding.lifecycleOwner = this
        binding.getYearListFragmentViewmodel = viewModel
        mContext = requireActivity()
        viewModel.bookNavigator = this
        initDeclaration(binding)
        // Inflate the layout for this fragment
        return binding.root
    }


    /***
     * Alternative soultion whicch json could parse directly on data model
     */
    private fun getMapAddressLocation() {
        val url = BuildConfig.ServerURL
        viewModel.isLoading.value = true

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create<RetrofitMaps>(RetrofitMaps::class.java!!)


        val call = service.getList()

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                viewModel.isLoading.value = false
                requireActivity().showToast(t.message.toString())
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                //your raw string response
                val stringResponse = response.body()?.string()
                val youth = stringResponse
                try {

                    val gson = Gson()
                    val jsonOutput = youth

                    val listType: Type = object : TypeToken<List<GetListAddress?>?>() {}.type
                    val posts: List<GetListAddress> =
                        gson.fromJson<List<GetListAddress>>(jsonOutput, listType)
                    var count = 0
                    var length = 0


                    val recyclerViewItems: ArrayList<GetAddress> = ArrayList()
                    for (value in posts) {

                        Log.v("JsonString", posts.toString())
                        count += value.price.toDouble().toInt()
                        binding.tvTotalPrice.text = "Total Price: $count"


                        for (response in value.locaionResponse) {
                            length += 1

                            binding.tvTotalCount.text = "Total Count: ${length.toString()}"
                            recyclerViewItems.add(response)
                        }
                    }



                    viewModel._itemList.value = recyclerViewItems
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
                    val adapter = RecyclerViewAdapter(viewModel._itemList, object :
                        RecyclerViewAdapter.OnItemClickListener {
                        override fun onItemClick(
                            latA: Double,
                            longitude: Double,
                            longitude1: Double,
                            latitude: Double,
                            locA: String,
                            locB: String
                        ) {
                            requireActivity().showToast("clicked")
                            sharePrefStorage.mapALocation = locA
                            sharePrefStorage.mapBLocation = locB
                            sharePrefStorage.latAValue = id.toString()
                            sharePrefStorage.latBVAlue = longitude1.toString()
                            sharePrefStorage.lngAValue = longitude.toString()
                            sharePrefStorage.lngBValue = latitude.toString()

                            findNavController().navigate(
                                GetYearlistFragmentDirections.actionMapFragment(
                                    "getyearlistnavigation",
                                    ""
                                )
                            )
                        }
                    })
                    binding.recyclerView.adapter = adapter
                    val dataObserver: Observer<ArrayList<GetAddress>> = Observer {
                        val dataObserver: Observer<ArrayList<GetAddress>> = Observer {
                            items.value = it
                            val adapter = RecyclerViewAdapter(items, object :
                                RecyclerViewAdapter.OnItemClickListener {
                                override fun onItemClick(
                                    id: Double,
                                    longitude: Double,
                                    longitude1: Double,
                                    latitude: Double,
                                    locA: String,
                                    locB: String
                                ) {
                                    requireActivity().showToast("clicked")

                                    sharePrefStorage.mapALocation = locA
                                    sharePrefStorage.mapBLocation = locB
                                    sharePrefStorage.latAValue = id.toString()
                                    sharePrefStorage.latBVAlue = longitude1.toString()
                                    sharePrefStorage.lngAValue = longitude.toString()
                                    sharePrefStorage.lngBValue = latitude.toString()

                                    findNavController().navigate(
                                        GetYearlistFragmentDirections.actionMapFragment(
                                            "getyearlistnavigation",
                                            ""
                                        )
                                    )
                                }
                            })
                            binding.recyclerView.adapter = adapter
                        }
                        viewModel._itemList.observe(requireActivity(), dataObserver)

                        viewModel.isLoading.value = false
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                    viewModel.isLoading.value = false
                    Log.d("TAG", "No valid json")
                }
            }

        })

    }


    @SuppressLint("SetTextI18n")
    private fun initDeclaration(binding: FragmentGetYearlistBinding) {
        // viewModel.onLocationRequestAPI()

        val args = requireArguments().getString("fragment_name")
        if (args != null) {

            if (args == "yearlistffragment") {
                getMapAddressLocation()
            } else {
                locLabel = requireArguments().getString("location_type")
                getmapCachedLocation()
            }

        }

        //locationGetById()

    }

    /***
     * Alternative soultion whicch json could parse directly on data model
     */
    private fun getmapCachedLocation() {
        val url = BuildConfig.ServerURL
        viewModel.isLoading.value = true

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create<RetrofitMaps>(RetrofitMaps::class.java!!)


        val call = service.getList()

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                viewModel.isLoading.value = false
                requireActivity().showToast(t.message.toString())
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                //your raw string response
                val stringResponse = response.body()?.string()
                val youth = stringResponse
                try {

                    val gson = Gson()
                    val jsonOutput = youth

                    val listType: Type = object : TypeToken<List<GetListAddress?>?>() {}.type
                    val posts: List<GetListAddress> =
                        gson.fromJson<List<GetListAddress>>(jsonOutput, listType)
                    var count = 0
                    var length = 0


                    val recyclerViewItems: ArrayList<CashedList> = ArrayList()
                    for (value in posts) {

                        Log.v("JsonString", posts.toString())
                        count += value.price.toDouble().toInt()
                        binding.tvTotalPrice.text = "Total Price: $count"


                        for (response in value.locaionResponse) {
                            length += 1

                            binding.tvTotalCount.text = "Total Count: ${length.toString()}"
                            recyclerViewItems.add(CashedList(response.a,"",response.month,response.price))
                        }
                    }



                    viewModel.cashedList.value = recyclerViewItems
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
                    val adapter = CachedAdapter(viewModel.cashedList, object :
                        CachedAdapter.OnItemClickListener {
                        override fun onItemClick(
                            latA: Double,
                            longitude: Double,

                           locA: String

                        ) {
                            requireActivity().showToast("clicked")
                            Log.v("location_label",locLabel!!)
                            if (locLabel.equals("Location A")) {
                                sharePrefStorage.mapALocation = locA
                                sharePrefStorage.latAValue = id.toString()
                                sharePrefStorage.lngAValue = longitude.toString()
                            } else {
                                sharePrefStorage.mapBLocation = locA
                                sharePrefStorage.latBVAlue = id.toString()
                                sharePrefStorage.lngBValue = longitude.toString()
                            }

                            findNavController().navigate(
                                GetYearlistFragmentDirections.actionMapFragment(
                                    "Cachedlistnavigation",
                                    locLabel!!
                                )
                            )
                        }
                    })
                    binding.recyclerView.adapter = adapter
                    val dataObserver: Observer<ArrayList<CashedList>> = Observer {
                        val dataObserver: Observer<ArrayList<CashedList>> = Observer {
                            cachedLocation.value = it
                            val adapter = CachedAdapter(cachedLocation, object :
                                CachedAdapter.OnItemClickListener {
                                override fun onItemClick(
                                    id: Double,
                                    longitude: Double,
                                    locA: String,

                                ) {
                                    requireActivity().showToast("clicked")

                                    Log.v("location_label",locLabel!!)

                                    if (locLabel.equals("Location A")) {
                                        sharePrefStorage.mapALocation = locA
                                        sharePrefStorage.latAValue = id.toString()
                                        sharePrefStorage.lngAValue = longitude.toString()
                                    } else {
                                        sharePrefStorage.mapBLocation = locA
                                        sharePrefStorage.latBVAlue = id.toString()
                                        sharePrefStorage.lngBValue = longitude.toString()
                                    }

                                    findNavController().navigate(
                                        GetYearlistFragmentDirections.actionMapFragment(
                                            "Cachedlistnavigation",
                                            locLabel!!
                                        )
                                    )
                                }
                            })
                            binding.recyclerView.adapter = adapter
                        }
                        viewModel.cashedList.observe(requireActivity(), dataObserver)

                        viewModel.isLoading.value = false
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                    viewModel.isLoading.value = false
                    Log.d("TAG", "No valid json")
                }
            }

        })

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GetYearlistFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onSuccess(getAddress: GetListAddress) {
    }

    fun setItemList(recyclerViewItems: ArrayList<GetAddress>) {
        viewModel._itemList.value = recyclerViewItems
    }

    override fun navigation() {
        TODO("Not yet implemented")
    }

    override fun onErrorMsg(errorMsg: String) {
        requireActivity().showToast(errorMsg)
    }


}