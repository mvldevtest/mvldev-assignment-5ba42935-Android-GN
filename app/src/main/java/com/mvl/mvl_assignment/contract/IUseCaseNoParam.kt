package com.mvl.mvl_assignment.contract

import com.mvl.mvl_assignment.helper.IOTaskResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

interface IUseCaseNoParam <out O : Any> {

    val repository: IRepository

    @ExperimentalCoroutinesApi
    suspend fun execute(): Flow<IOTaskResult<O>>
}