package com.mvl.mvl_assignment.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import com.mvl.mvl_assignment.data.model.request.A
import com.mvl.mvl_assignment.data.model.request.AddLocationRequest
import com.mvl.mvl_assignment.data.model.request.B
import com.mvl.mvl_assignment.data.model.response.GetAddress

import com.mvl.mvl_assignment.data.di.RoomDBRepository
import com.mvl.mvl_assignment.domain.GetSingleLocationRequest
import com.mvl.mvl_assignment.domain.SendLocationRequest
import com.mvl.mvl_assignment.helper.*

import com.mvl.mvl_assignment.ui.view.interfaces.MapNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapFragmentViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val roomDBRepository: RoomDBRepository,
    private val sendLocationRequest: SendLocationRequest,
    private val networkHelper: NetworkHelper,
    private val getSingleLocationRequest: GetSingleLocationRequest
) : BaseViewModel() {

    @SuppressLint("StaticFieldLeak")
    val mContext = context
    val airQualityValue = MutableLiveData<String>("")
    var isLoading = MutableLiveData<Boolean>()
    val valueALocation = MutableLiveData<String>("Location A")
    val btnTextChange = MutableLiveData<String>("Set A")
    val valueBLocation = MutableLiveData<String>("Location B")
    val locationAddress = MutableLiveData<String>("")
    val buttonValue = MutableLiveData<String>("")


    @SuppressLint("StaticFieldLeak")
    var sharePrefStorage: SharePrefStorage = SharePrefStorage()


    /**
     * Location Response
     */
    val getSingleLocationResponse: MutableLiveData<ViewState<GetAddress>> by lazy {
        MutableLiveData<ViewState<GetAddress>>()
    }

    /**
     * Location Response
     */
    val getLocationResponse: MutableLiveData<ViewState<GetAddress>> by lazy {
        MutableLiveData<ViewState<GetAddress>>()
    }

    init {
        viewModelScope.launch {
            val runningAMap: com.mvl.mvl_assignment.model.GetAddress = com.mvl.mvl_assignment.model.GetAddress(
                0L,
                "11"!!,
                "2022",
                "sharePrefStorage.mapALocation!!",
                "sharePrefStorage.mapBLocation!!",
                10.234,
                11.234,10.234,
                11.234,10,
                11,
                "30000"
            )
            roomDBRepository.insertLocation(runningAMap)
        }
    }


    fun onClickChange() {
        if (btnTextChange.value.equals("Set A")) {
            valueALocation.value = "Location A : " + locationAddress.value.toString()
            btnTextChange.value = "Set B"
            sharePrefStorage.mapALocation = locationAddress.value.toString()
        } else if (btnTextChange.value.equals("Set B")) {
            if (sharePrefStorage.lngBValue.isNullOrEmpty() && sharePrefStorage.latBVAlue.isNullOrEmpty()) {
                Toast.makeText(
                    mContext,
                    "You need to select the destination location",
                    Toast.LENGTH_LONG
                ).show()
            } else if (String.format(
                    "%.4f",
                    sharePrefStorage.lngAValue!!.toDouble()
                ) == String.format("%.4f", sharePrefStorage.lngBValue!!.toDouble())
                && String.format(
                    "%.4f",
                    sharePrefStorage.latAValue!!.toDouble()
                ) == String.format("%.4f", sharePrefStorage.latBVAlue!!.toDouble())
            ) {
                Toast.makeText(
                    mContext,
                    "You have selected the same location kindly change the location to continue",
                    Toast.LENGTH_LONG
                ).show()
            } else {

                valueBLocation.value = "Location B : " + locationAddress.value.toString()
                btnTextChange.value = "Book"
                sharePrefStorage.mapBLocation = locationAddress.value.toString()
            }
        } else {
            mainNavigator.mainNavigator!!.LocationAddOnClick()
        }
    }

    object mainNavigator {
        var mainNavigator: MapNavigator? = null
    }



    /**
     * Get Location request once  submit the phone number for OTP
     */
    @ExperimentalCoroutinesApi
    fun onLocationSingleRequestAPI(id: String) {
        isLoading.value = true
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                getViewStateFlowForNetworkCall {
                    val id: String? = id
                    showLog("verify", id.toString())
                    getSingleLocationRequest.execute(id!!)
                }.collect {
                    when (it) {
                        is ViewState.Loading -> getLocationResponse.value = it
                        is ViewState.RenderFailure -> {
                            Toast.makeText(mContext, Constants.ERROR_SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show()
                            isLoading.value = false


                        }
                        is ViewState.RenderSuccess<GetAddress> -> {
                            isLoading.value = false
                            valueALocation.value="Location A: "+it.output.a.name
                            valueBLocation.value="Location B: "+it.output.b.name

                            mainNavigator.mainNavigator?.onGetAddressSingle(it.output.a.name,it.output.a.latitude,it.output.a.longitude)



                        }
                    }
                }
            } else {

                MapFragmentViewModel.mainNavigator.mainNavigator!!.onerrorMsg(Constants.ERROR_SOCKET_TIMEOUT_EXCEPTION)
                isLoading.value = false

                Log.v("output", "no network")
            }
        }
    }


    /**
     * Get Location request
     */
    @ExperimentalCoroutinesApi
    fun onLocationRequestAPI(year: String, month: String?) {
        isLoading.value = true
        viewModelScope.launch {
            insertMapLocation(month,year)
            if (networkHelper.isNetworkConnected()) {
                getViewStateFlowForNetworkCall {
                    val addLocationRequest: AddLocationRequest? = AddLocationRequest(
                        A(
                            sharePrefStorage.mapQAValue!!.toInt(),
                            sharePrefStorage.latAValue!!.toDouble(),
                            sharePrefStorage.lngAValue!!.toDouble(),
                            sharePrefStorage.mapALocation!!
                        ), B(
                            sharePrefStorage.mapQBValue!!.toInt(),
                            sharePrefStorage.latBVAlue!!.toDouble(),
                            sharePrefStorage.lngBValue!!.toDouble(),
                            sharePrefStorage.mapBLocation!!
                        ), year, month!!
                    )

                    showLog("verify", addLocationRequest.toString())
                    sendLocationRequest.execute(addLocationRequest!!)
                }.collect {
                    when (it) {
                        is ViewState.Loading -> getLocationResponse.value = it
                        is ViewState.RenderFailure -> {
                            mainNavigator.mainNavigator!!.onerrorMsg(Constants.ERROR_SOMETHING_WENT_WRONG)
                            isLoading.value = false
                            getLocationResponse.value = it
                            mainNavigator.mainNavigator!!.verifyLocationSuccess(
                                "Success",
                                "5"
                            )

                        }
                        is ViewState.RenderSuccess<GetAddress> -> {
                            isLoading.value = false
                            insertMapLocation(month,year)
                            mainNavigator.mainNavigator!!.verifyLocationSuccess(
                                "Success",
                                it.output.id
                            )
                        }
                    }
                }
            } else {
                insertMapLocation(month,year)
                mainNavigator.mainNavigator!!.onerrorMsg(Constants.ERROR_SOCKET_TIMEOUT_EXCEPTION)
                isLoading.value = false
                Log.v("output", "no network")
            }
        }
    }

    /**
     * Get webclient
     */


    companion object MapCompanionView {
        @SuppressLint("onTouchListener", "ClickableViewAccessibility")
        @JvmStatic
        @BindingAdapter("touchListener")
        fun setTouchListener(self: View, value: Boolean) {
            self.setOnTouchListener { view, event -> // Check if the button is PRESSED
                if (event.action == MotionEvent.ACTION_DOWN) {
                    mainNavigator!!.mainNavigator!!.navigation("Location value A")
                } // Check if the button is RELEASED
                else if (event.action == MotionEvent.ACTION_UP) {
                    //do some thing
                }
                false
            }
        }

        @SuppressLint("onTouchListener", "ClickableViewAccessibility")
        @JvmStatic
        @BindingAdapter("touchBListener")
        fun setTouchBListener(self: View, value: Boolean) {
            self.setOnTouchListener { view, event -> // Check if the button is PRESSED
                if (event.action == MotionEvent.ACTION_DOWN) {
                    mainNavigator!!.mainNavigator!!.navigation("Location value B")
                } // Check if the button is RELEASED
                else if (event.action == MotionEvent.ACTION_UP) {
                    //do some thing
                }
                false
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
     fun insertMapLocation(month: String?, year: String) {
       viewModelScope.launch {
           val runningAMap: com.mvl.mvl_assignment.model.GetAddress = com.mvl.mvl_assignment.model.GetAddress(
               0L,
               month!!,
               year,
               sharePrefStorage.mapALocation!!,
               sharePrefStorage.mapBLocation!!,
               sharePrefStorage.latAValue!!.toDouble(),
               sharePrefStorage.lngAValue!!.toDouble(),
               sharePrefStorage.latBVAlue!!.toDouble(),
               sharePrefStorage.lngBValue!!.toDouble(),
               sharePrefStorage.mapQAValue!!.toInt(),
               sharePrefStorage.mapQBValue!!.toInt(),
               "30000"
           )
           roomDBRepository.insertLocation(runningAMap)
       }
    }
}