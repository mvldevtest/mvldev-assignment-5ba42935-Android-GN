package com.mvl.mvl_assignment.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mvl.mvl_assignment.model.GetAddress


@Dao
interface MapDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun mapInsert(runningMap: GetAddress)

    @Query("SELECT * FROM runningMap WHERE id=:locId")
    fun getLocationById(locId: Int): LiveData<List<GetAddress>>

    @Query("SELECT * FROM runningMap WHERE month=:month AND year=:year")
    fun getLocationByMonth(month: String, year: String): LiveData<List<GetAddress>>

    @Query("SELECT * FROM runningMap")
    fun getallLocation(): LiveData<MutableList<GetAddress>>


}