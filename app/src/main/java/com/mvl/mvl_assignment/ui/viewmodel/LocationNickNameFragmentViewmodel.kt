package com.mvl.mvl_assignment.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout
import com.mvl.mvl_assignment.helper.NetworkHelper
import com.mvl.mvl_assignment.helper.SharePrefStorage
import com.mvl.mvl_assignment.ui.view.interfaces.MapNavigator
import com.mvl.mvl_assignment.ui.view.interfaces.NickNameNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class LocationNickNameFragmentViewmodel @Inject constructor(
    @ApplicationContext context: Context,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val address = MutableLiveData<String>("")
    val qualityValue = MutableLiveData<String>("")
    val nickName = MutableLiveData<String>("")
    val nameErrorMessage = MutableLiveData("")
    val locationLabel = MutableLiveData("")
    @SuppressLint("StaticFieldLeak")
    var sharePrefStorage: SharePrefStorage = SharePrefStorage()

   var  nickNameNavigation: NickNameNavigator?=null


    fun onClick() {
        if (nickName.value.isNullOrEmpty()) {
            nameErrorMessage.value = "Please enter the nick name"
        } else {
            if (locationLabel.value.equals("Location A")) {
                sharePrefStorage.Loc_NickName_A=nickName.value
                nickNameNavigation!!.navigationToMapFragment("Location A")
            }else if(locationLabel.value.equals("Location B")){
                sharePrefStorage.Loc_NickName_b=nickName.value
                nickNameNavigation!!.navigationToMapFragment("Location B")
            }
        }
    }

    companion object  {
        @JvmStatic
        @BindingAdapter("app:errorText")
        fun setErrorText(view: TextInputLayout, errorMessage: String) {
            if (errorMessage.isEmpty())
                view.error = null
            else
                view.error = errorMessage;
        }
    }


}