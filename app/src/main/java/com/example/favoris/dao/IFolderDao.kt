package com.example.favoris.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.favoris.model.Folder
import androidx.room.Dao as Dao

@Dao
interface IFolderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFolder (folder: Folder)
}