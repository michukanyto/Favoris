package com.example.favoris.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks",foreignKeys = [
ForeignKey(entity = Folder::class,
    parentColumns = ["id"],
    childColumns = ["id"],
    onDelete = ForeignKey.NO_ACTION,
    onUpdate = ForeignKey.NO_ACTION)])
data class Bookmark (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val folderId: Long,
    val name: String,
    val url: String
)