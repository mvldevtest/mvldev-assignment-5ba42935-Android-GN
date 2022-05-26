package com.mvl.mvl_assignment.data.model.response

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class GetListAddress (

    val month: String,
    val price: String,

    @SerializedName( "Locaion Response")
    val locaionResponse: List<GetAddress>,

    val id: String,
   // val a: A? = null,
   // val b: A? = null,
    val year: String
)




