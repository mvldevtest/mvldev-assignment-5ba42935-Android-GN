package com.mvl.mvl_assignment.domain



import com.mvl.mvl_assignment.contract.IRepository
import com.mvl.mvl_assignment.contract.IUseCase
import com.mvl.mvl_assignment.data.model.response.GetAddress
import com.mvl.mvl_assignment.helper.IOTaskResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSingleLocationRequest @Inject constructor(override val repository: IRepository) :
    IUseCase<String, GetAddress> {

    @ExperimentalCoroutinesApi
    override suspend fun execute(input: String): Flow<IOTaskResult<GetAddress>> =
            repository.getSingleLocationRequest(input)




}
