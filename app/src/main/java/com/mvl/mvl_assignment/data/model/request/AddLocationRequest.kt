package com.mvl.mvl_assignment.data.model.request



data class AddLocationRequest(
    val a: A,
    val b: B,
    val year:String,
    val month:String
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