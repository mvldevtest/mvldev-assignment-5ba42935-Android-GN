package com.mvl.mvl_assignment.network




import com.mvl.mvl_assignment.contract.IRemoteDataSource
import com.mvl.mvl_assignment.contract.IRepository
import com.mvl.mvl_assignment.data.model.request.AddLocationRequest
import com.mvl.mvl_assignment.data.model.response.GetAddress
import com.mvl.mvl_assignment.data.model.response.GetListAddress
import com.mvl.mvl_assignment.helper.IOTaskResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository class impl from [IRepository]
 * @author Prasan
 * @since 1.0
 * @see IRepository
 * @see IRemoteDataSource
 */
@Singleton
class FHPRepository @Inject constructor(override val remoteDataSource: IRemoteDataSource) :
    IRepository {

    @ExperimentalCoroutinesApi
    override suspend fun provideAddLocation(addLocationRequest: AddLocationRequest): Flow<IOTaskResult<GetAddress>> =
        remoteDataSource.provideAddLocation(addLocationRequest)

    @ExperimentalCoroutinesApi
    override suspend fun getSingleLocationRequest(id: String): Flow<IOTaskResult<GetAddress>> =
        remoteDataSource.getSingleLocationRequest(id)

    @ExperimentalCoroutinesApi
    override suspend fun getListAddress(): Flow<IOTaskResult<GetListAddress>> =remoteDataSource.getListAddress()




}