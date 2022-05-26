package com.mvl.mvl_assignment.model

import com.mvl.mvl_assignment.data.model.response.A


data class CashedList(
    val a: A,
    val id: String,
    val month: String,
    val price: String
)

data class A(
    val aqi: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String
)
