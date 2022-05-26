package com.mvl.mvl_assignment.ui.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mvl.mvl_assignment.data.model.response.GetAddress
import com.mvl.mvl_assignment.helper.*
import com.mvl.mvl_assignment.ui.view.interfaces.BookNavigator
import com.mvl.mvl_assignment.domain.GetSingleLocationRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestBookFragmentViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val networkHelper: NetworkHelper,
    private val getSingleLocationRequest: GetSingleLocationRequest
) : BaseViewModel() {
    var bookNavigator: BookNavigator? = null
    val valueALocation = MutableLiveData<String>("")
    val valueBLocation = MutableLiveData<String>("")
    val valueQCA = MutableLiveData<String>("")
    val valueQCB = MutableLiveData<String>("")
    val price = MutableLiveData<String>("")
    var isLoading = MutableLiveData<Boolean>()
    val mContext=context
    /**
     * Location Response
     */
    val getLocationResponse: MutableLiveData<ViewState<GetAddress>> by lazy {
        MutableLiveData<ViewState<GetAddress>>()
    }

    fun onClickNavigation(){
        bookNavigator!!.navigation()

    }


    /**
     * Get Location request once  submit the phone number for OTP
     */
    @ExperimentalCoroutinesApi
    fun onLocationRequestAPI(id: String) {
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
                            valueQCA.value="Quality Air Value A: "+it.output.a.aqi.toString()
                            valueBLocation.value="Location B: "+it.output.b.name
                            valueQCB.value="Quality Air Value: "+it.output.b.aqi.toString()
                            price.value="Price: "+it.output.price.toString()

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

}