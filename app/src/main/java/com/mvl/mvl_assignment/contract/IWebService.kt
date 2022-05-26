package com.mvl.mvl_assignment.contract



import com.mvl.mvl_assignment.data.model.request.AddLocationRequest
import com.mvl.mvl_assignment.data.model.response.GetAddress
import com.mvl.mvl_assignment.data.model.response.GetListAddress
import com.mvl.mvl_assignment.helper.IOTaskResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

/**
 * This interface provides contracts a web-service class needs to abide by to provide the app
 * with network data as required
 * @author Prasan
 * @since 1.0
 */
interface IWebService {

   /* *//**
     * Performs the popular photos API call. In an offline-first architecture, it is at this function
     * call that the Repository class would check if the data for the given page number exists in a Room
     * table, if so return the data from the db, else perform a retrofit call to obtain and store the data
     * into the db before returning the same
     * @param pageNumber Page number of the data called in a paginated data source
     * @return [IOTaskResult] of [PhotoResponse] type
     */
    @ExperimentalCoroutinesApi
    suspend fun provideAddLocation(
            loginRequest: AddLocationRequest
    ): Flow<IOTaskResult<GetAddress>>

    @ExperimentalCoroutinesApi
    suspend fun getSingleLocationRequest(
        id:String
    ): Flow<IOTaskResult<GetAddress>>

    @ExperimentalCoroutinesApi
    suspend fun getListAddress(): Flow<IOTaskResult<GetListAddress>>


}