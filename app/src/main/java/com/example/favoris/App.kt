package com.example.favoris

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase

class App : Application() {
    companion object{
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this,AppDatabase::class.java,"Favoris").build()
    }

}