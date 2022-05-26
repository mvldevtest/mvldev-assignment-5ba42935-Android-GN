package com.mvl.mvl_assignment.network




import com.mvl.mvl_assignment.contract.IWebService
import com.mvl.mvl_assignment.data.model.request.AddLocationRequest
import com.mvl.mvl_assignment.data.model.request.GetSingleLocationRequest
import com.mvl.mvl_assignment.data.model.response.GetAddress
import com.mvl.mvl_assignment.data.model.response.GetListAddress
import com.mvl.mvl_assignment.helper.IOTaskResult
import com.mvl.mvl_assignment.helper.performSafeNetworkApiCall
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [IWebService] impl class which uses Retrofit to provide the app with the functionality to make
 * network requests
 * @author Prasan
 * @since 1.0
 * @see FiveHundredPixelsAPI
 * @see [IWebService]
 */
@Singleton
class RetrofitWebService @Inject constructor(private val retrofitClient: FiveHundredPixelsAPI) :
    IWebService {



    @ExperimentalCoroutinesApi
    override suspend fun provideAddLocation(addLocationRequest: AddLocationRequest): Flow<IOTaskResult<GetAddress>> =
        performSafeNetworkApiCall("Error Obtaining Photos") {
            retrofitClient.sendLocationAddRequest(
                addLocationRequest = addLocationRequest
            )
        }

    @ExperimentalCoroutinesApi
    override suspend fun getSingleLocationRequest(id: String): Flow<IOTaskResult<GetAddress>> =
        performSafeNetworkApiCall("Error Obtaining Photos") {
            retrofitClient.getSingleLocationAPI(
                    id = id
            )
        }

    @ExperimentalCoroutinesApi
    override suspend fun getListAddress(): Flow<IOTaskResult<GetListAddress>> =
        performSafeNetworkApiCall("Error Obtaining Photos") {
            retrofitClient.getListAddress()
        }





}