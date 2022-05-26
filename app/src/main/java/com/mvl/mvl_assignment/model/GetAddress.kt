package com.mvl.mvl_assignment.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "runningMap")
data class GetAddress(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var month:String,
    var year:String,
    var nameA: String,
    var nameB: String,
    var latitudeA: Double = 0.0,
    var longitudeA: Double = 0.0,
    var latitudeB: Double = 0.0,
    var longitudeB: Double = 0.0,
    var aqiA: Int = 0,
    var aqiB: Int = 0,
    var Price:String
)