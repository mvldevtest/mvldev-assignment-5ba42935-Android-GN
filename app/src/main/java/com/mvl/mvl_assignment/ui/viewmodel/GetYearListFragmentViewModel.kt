package com.mvl.mvl_assignment.ui.viewmodel


import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.mvl.mvl_assignment.data.model.response.GetAddress
import com.mvl.mvl_assignment.data.model.response.GetListAddress
import com.mvl.mvl_assignment.data.di.RoomDBRepository
import com.mvl.mvl_assignment.domain.GetListAddressByYear
import com.mvl.mvl_assignment.helper.Constants
import com.mvl.mvl_assignment.helper.NetworkHelper
import com.mvl.mvl_assignment.helper.ViewState
import com.mvl.mvl_assignment.helper.getViewStateFlowForNetworkCall
import com.mvl.mvl_assignment.local.MapDao
import com.mvl.mvl_assignment.model.CashedList

import com.mvl.mvl_assignment.ui.view.interfaces.BookNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetYearListFragmentViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val networkHelper: NetworkHelper,
    val roomDBRepository: RoomDBRepository,
    private val getListAddressYear: GetListAddressByYear
) : BaseViewModel() {

    var _itemList = MutableLiveData<ArrayList<GetAddress>>()
    var cashedList = MutableLiveData<ArrayList<CashedList>>()
    var iterateData: LiveData<MutableList<MapDao>> = MutableLiveData<MutableList<MapDao>>()

    var bookNavigator: BookNavigator? = null
    var isLoading = MutableLiveData<Boolean>()

    /**
     * Location Response
     */
    val getLocationList: MutableLiveData<ViewState<GetListAddress>> by lazy {
        MutableLiveData<ViewState<GetListAddress>>()
    }




    @ExperimentalCoroutinesApi
    fun onLocationRequestAPI() {
        isLoading.value = true
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                getViewStateFlowForNetworkCall {
                    getListAddressYear.execute()
                }.collect {
                    when (it) {
                        is ViewState.Loading -> getLocationList.value = it
                        is ViewState.RenderFailure -> {
                            bookNavigator!!.onErrorMsg(Constants.ERROR_SOMETHING_WENT_WRONG)
                            isLoading.value = false
                            getLocationList.value = it
                        }
                        is ViewState.RenderSuccess<GetListAddress> -> {
                            isLoading.value = false
                            bookNavigator!!.onSuccess(it.output)!!

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




    /*@OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    suspend fun getLocationByYear() {
        viewModelScope.launch {
            val recyclerViewItems: ArrayList<RunningMap> = ArrayList()
            //val itemList = roomDBRepository.fetchLocationByMonth("11", "2022")
            iterateData = roomDBRepository.fetchLocationList()




            for (value in iterateData.value!!.iterator()) {
                val runningAMap: RunningMap = RunningMap(
                    0L,
                    value.month!!,
                    value.year,
                    value.nameA!!,
                    value.nameB!!,
                    value.latitudeA,
                    value.longitudeA,
                    value.latitudeB,
                    value.longitudeB,
                    value.aqiA,
                    value.aqiB,
                    value.Price
                )
                recyclerViewItems.add(runningAMap)
            }
            _itemList.value = recyclerViewItems
        }
    }*/


}