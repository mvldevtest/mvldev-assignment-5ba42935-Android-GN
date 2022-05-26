package com.mvl.mvl_assignment.contract


import com.mvl.mvl_assignment.helper.IOTaskResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

/**
 * A UseCase defines a specific task performed in the app. For this project there would be two:
 * 1. Get Popular Photos
 * 2. Get details of a photo
 * [O] type defines the output of the use-case execution
 * @author Prasan
 */
interface IUseCase<in I : Any, out O : Any> {

    val repository: IRepository

    @ExperimentalCoroutinesApi
    suspend fun execute(input: I): Flow<IOTaskResult<O>>
}