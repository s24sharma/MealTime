package com.sasha.myapplication.databaseRes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Restaurant")
data class ResEntity(
    @PrimaryKey val res_id: Int,
    @ColumnInfo(name="res_name") val name:String,
    @ColumnInfo(name="res_rating") val rating: String,
    @ColumnInfo(name="res_cost") val cost_for_one: String,
    @ColumnInfo(name="res_image") val image_url:String
)



