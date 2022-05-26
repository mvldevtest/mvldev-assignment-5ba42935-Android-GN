package com.mvl.mvl_assignment.data.di


import com.mvl.mvl_assignment.local.MapDao
import com.mvl.mvl_assignment.model.GetAddress
import javax.inject.Inject

class RoomDBRepository @Inject  constructor(private val mapDao: MapDao){
    suspend fun  insertLocation(runningMap: GetAddress) = mapDao.mapInsert(runningMap)
    suspend fun  fetchLocationById(locId:Int) = mapDao.getLocationById(locId)
    suspend fun  fetchLocationByMonth(month:String,year:String) = mapDao.getLocationByMonth(month,year)
    suspend fun  fetchLocationList() = mapDao.getallLocation()
}