package com.mvl.mvl_assignment.contract

import com.mvl.mvl_assignment.data.model.request.AddLocationRequest
import com.mvl.mvl_assignment.data.model.response.GetAddress
import com.mvl.mvl_assignment.data.model.response.GetListAddress
import com.mvl.mvl_assignment.helper.IOTaskResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow


/**
 * The repository class represents the data store of the application. This class is primarily utilised
 * when building offline-first applications where it will make the determination to load the data from
 * a local Room DB vs calling the retrofit function in order to obtain the data
 * @author Prasan
 * @since 1.0
 * @see IRemoteDataSource
 */
interface IRepository {

    val remoteDataSource: IRemoteDataSource

    @ExperimentalCoroutinesApi
    suspend fun provideAddLocation(addLocationRequest: AddLocationRequest): Flow<IOTaskResult<GetAddress>>

    @ExperimentalCoroutinesApi
    suspend fun getSingleLocationRequest(id:String): Flow<IOTaskResult<GetAddress>>

    @ExperimentalCoroutinesApi
    suspend fun getListAddress(): Flow<IOTaskResult<GetListAddress>>

 /*   @ExperimentalCoroutinesApi
    suspend fun setOTPApi(loginRequest: LoginRequest): Flow<IOTaskResult<LoginResponse>>

    @ExperimentalCoroutinesApi
    suspend fun setVerifyOtp(verifyOtpRequest: VerifyOtpRequest): Flow<IOTaskResult<VerifyOtpResponse>>

    @ExperimentalCoroutinesApi
    suspend fun autoLoginApi(autoLoginRequest: AutoLoginRequest): Flow<IOTaskResult<VerifyOtpResponse>>


    @ExperimentalCoroutinesApi
    suspend fun LogoutApi(logoutRequest: LogoutRequest): Flow<IOTaskResult<LogoutResponse>>*/

}