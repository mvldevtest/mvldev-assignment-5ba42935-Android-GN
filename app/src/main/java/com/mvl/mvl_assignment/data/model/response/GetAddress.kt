package com.mvl.mvl_assignment.data.model.response

data class GetAddress(
    val a: A,
    val b: B,
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

data class B(
    val aqi: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String
)