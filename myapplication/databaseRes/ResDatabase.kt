package com.sasha.myapplication.databaseRes

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ResEntity::class, OrderEntity::class],version = 1)
abstract class ResDatabase : RoomDatabase(){

    abstract fun resDao():ResDao
    abstract fun orderDao(): OrderDao
}