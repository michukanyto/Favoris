package com.example.favoris

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.favoris.dao.IBookmarkDao
import com.example.favoris.dao.IFolderDao
import com.example.favoris.model.Bookmark
import com.example.favoris.model.Folder


@Database(entities = [Folder::class, Bookmark::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun folderDAO() : IFolderDao
    abstract  fun bookmarkDAO() : IBookmarkDao
}