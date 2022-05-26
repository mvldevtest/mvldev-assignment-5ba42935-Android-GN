package com.mvl.mvl_assignment.maphelper.locationhelper.interfaces


import com.mvl.mvl_assignment.maphelper.map.model.AQIModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Created by sumasoft on 26/09/17.
 */

interface RetrofitMaps {
    @GET("feed/geo:{lat};{lng}/?token=cebbc6885ef4b0364c2622a8366349424c9e7f74")
    fun getAqiValue(@Path("lat") lat: Double?,@Path("lng") lng: Double?): Call<AQIModel>

    @GET("api/v1/test")
    fun getList(): Call<ResponseBody>
}

