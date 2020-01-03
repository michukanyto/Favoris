package com.example.favoris.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.favoris.model.Bookmark

@Dao
interface IBookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookmark(bookmark: Bookmark)

    @Query("SELECT * FROM bookmarks")
    fun getAllBookmarks() : LiveData<List<Bookmark>>

    @Query("SELECT * FROM bookmarks WHERE id = :idBookmark")
    fun getBookmark(idBookmark: Long) : LiveData<Bookmark>
}