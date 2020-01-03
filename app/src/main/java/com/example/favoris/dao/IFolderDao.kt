package com.example.favoris.dao

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.favoris.model.Folder
import androidx.room.Dao as Dao

@Dao
interface IFolderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFolder (folder: Folder)

    @Query("SELECT * FROM folders")
    fun getAllFolders(): LiveData<List<Folder>>

    @Query("SELECT * FROM folders WHERE name = :folderName")
    fun getFolder(folderName: String): LiveData<Folder>
}