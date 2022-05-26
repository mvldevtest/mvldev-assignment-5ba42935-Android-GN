package com.mvl.mvl_assignment.domain


import com.mvl.mvl_assignment.contract.IRepository
import com.mvl.mvl_assignment.contract.IUseCase
import com.mvl.mvl_assignment.contract.IUseCaseNoParam
import com.mvl.mvl_assignment.data.model.response.GetAddress
import com.mvl.mvl_assignment.data.model.response.GetListAddress
import com.mvl.mvl_assignment.helper.IOTaskResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetListAddressByYear @Inject constructor(override val repository: IRepository) :
    IUseCaseNoParam<GetListAddress> {

    @ExperimentalCoroutinesApi
    override suspend fun execute(): Flow<IOTaskResult<GetListAddress>> =
        repository.getListAddress()



}
