package com.mvl.mvl_assignment.network




import com.mvl.mvl_assignment.data.model.request.AddLocationRequest
import com.mvl.mvl_assignment.data.model.response.GetAddress
import com.mvl.mvl_assignment.data.model.response.GetListAddress
import dagger.hilt.internal.GeneratedEntryPoint
import retrofit2.Response
import retrofit2.http.*
import javax.inject.Singleton

/**
 * Retrofit API class for the 500px API
 * @author Prasan
 * @since 1.0
 */
@Singleton
interface FiveHundredPixelsAPI {

    /**
     * Performs a GET call to obtain a paginated list of photos
     * @param key API Key
     * @param feature feature source the photos should come from
     * @param page Page number of the data where the photos should come from
     * @return [Response] instance of [PhotoResponse] type
     */
    @POST("api/v1/test")
    suspend fun sendLocationAddRequest(@Body addLocationRequest: AddLocationRequest): Response<GetAddress>

    @PUT("api/v1/test/{id}")
    suspend fun getSingleLocationAPI(@Path("id") id:String):Response<GetAddress>

    @GET("api/v1/test")
    suspend fun getListAddress():Response<GetListAddress>



}