package com.mvl.mvl_assignment.ui.view.interfaces

interface MapNavigator {

    fun onGetAddress()
    fun onGetAddressSingle(id: String, latitude: Double, longitude: Double)
    fun onGetAirQualityValue()
    fun navigation(locationLabel:String)
    fun LocationAddOnClick()
    fun onerrorMsg(errorMsg:String)
    fun verifyLocationSuccess(Status: String, id :String)

}