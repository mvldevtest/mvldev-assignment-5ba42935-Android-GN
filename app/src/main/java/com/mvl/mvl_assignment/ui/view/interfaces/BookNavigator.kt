package com.mvl.mvl_assignment.ui.view.interfaces

import com.mvl.mvl_assignment.data.model.response.GetAddress
import com.mvl.mvl_assignment.data.model.response.GetListAddress
import com.mvl.mvl_assignment.domain.GetListAddressByYear

interface BookNavigator {
   fun onSuccess(getAddress: GetListAddress)
    fun onErrorMsg(errorMsg:String)
    fun navigation()

}