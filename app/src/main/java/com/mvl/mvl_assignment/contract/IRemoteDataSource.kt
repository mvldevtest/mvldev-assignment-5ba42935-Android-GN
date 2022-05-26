package com.mvl.mvl_assignment.contract


import com.mvl.mvl_assignment.data.model.request.AddLocationRequest
import com.mvl.mvl_assignment.data.model.response.GetAddress
import com.mvl.mvl_assignment.data.model.response.GetListAddress
import com.mvl.mvl_assignment.helper.IOTaskResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

/**
 * Contract for the remote data source that will provide the plug to access network data by obtaining
 * an instance of [IWebService] interface in the implementing class
 * @author Prasan
 * @since 1.0
 * @see IWebService
 */
interface IRemoteDataSource {

    // Webservice Interface that a remote data source impl class needs to provide
    val webService: IWebService

    @ExperimentalCoroutinesApi
    suspend fun provideAddLocation(addLocationRequest: AddLocationRequest): Flow<IOTaskResult<GetAddress>>
    @ExperimentalCoroutinesApi
    suspend fun getSingleLocationRequest(id:String): Flow<IOTaskResult<GetAddress>>

   @ExperimentalCoroutinesApi
    suspend fun getListAddress(): Flow<IOTaskResult<GetListAddress>>

  /*   @ExperimentalCoroutinesApi
    suspend fun autoLoginApi(autoLoginRequest: AutoLoginRequest): Flow<IOTaskResult<VerifyOtpResponse>>

    @ExperimentalCoroutinesApi
    suspend fun LogoutApi(logoutRequest: LogoutRequest): Flow<IOTaskResult<LogoutResponse>>*/
}