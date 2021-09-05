package com.sasha.myapplication.databaseRes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface ResDao {

    @Insert
    fun insertRes(resEntity: ResEntity)

    @Delete
    fun deleteRes(resEntity: ResEntity)

    @Query("SELECT * FROM Restaurant")
    fun getAllRes():List<ResEntity>

    @Query("SELECT * FROM Restaurant WHERE res_id= :res_id")
    fun getResById(res_id:String): ResEntity
}