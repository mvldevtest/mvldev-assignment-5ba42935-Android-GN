package com.mvl.mvl_assignment.network


import com.mvl.mvl_assignment.contract.IRemoteDataSource
import com.mvl.mvl_assignment.contract.IWebService
import com.mvl.mvl_assignment.data.model.request.AddLocationRequest
import com.mvl.mvl_assignment.data.model.response.GetAddress
import com.mvl.mvl_assignment.data.model.response.GetListAddress
import com.mvl.mvl_assignment.helper.IOTaskResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [IRemoteDataSource] impl class that provides access to network API calls
 * @author Prasan
 * @since 1.0
 * @see IRemoteDataSource
 * @see IWebService
 */
@Singleton
class NetworkDataSource @Inject constructor(override val webService: IWebService) :
    IRemoteDataSource {


    @ExperimentalCoroutinesApi
    override suspend fun provideAddLocation(addLocationRequest: AddLocationRequest): Flow<IOTaskResult<GetAddress>> =
        webService.provideAddLocation(addLocationRequest)

    @ExperimentalCoroutinesApi
    override suspend fun getSingleLocationRequest(id: String): Flow<IOTaskResult<GetAddress>> =
        webService.getSingleLocationRequest(id)

    @ExperimentalCoroutinesApi
    override suspend fun getListAddress(): Flow<IOTaskResult<GetListAddress>> = webService.getListAddress()




}