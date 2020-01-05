package com.example.favoris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.favoris.dao.IBookmarkDao
import com.example.favoris.dao.IFolderDao
import com.example.favoris.model.Bookmark
import com.example.favoris.model.Folder
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private var folderName:String? = null
    private var bookmark:String? = null
    private var name:String? = null
    private var url:String? = null
    private var folderID:Long? = 0

//    private val folderDAO = App.dataBase.folderDAO()
//    private val bookmarkDAO = App.dataBase.bookmarkDAO()
    private lateinit var folderDAO: IFolderDao
    private lateinit var bookmarkDAO: IBookmarkDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        folderDAO = App.dataBase.folderDAO()
        createFolderButton.setOnClickListener {
            folderName = createFolderEditText.text.toString()
            saveFolder()
            bookmarkFolderNameEditText.setText(folderName)
            folderDAO.getFolder(folderName!!).observe(this, Observer { folder ->
                Log.i("Main1","folders = $folder")
                folderID = folder.id
                if (folder.name == folderName) {
                    displayToast("Folder created successfully")
                } else {
                    displayToast(" WARNING!!! Folder wasn't created ")
                }
            })
        }


        bookmarkDAO = App.dataBase.bookmarkDAO()
        createBookmarkButton.setOnClickListener {
            bookmark = bookmarkFolderNameEditText.text.toString()
            name = bookmarkNameEditText.text.toString()
            url = bookmarkUrlEditText.text.toString()
            createBookmark()
            bookmarkDAO.getAllBookmarks().observe(this, Observer { bookmarks ->
                bookmarksTextView.text = bookmarks.toString()
            })
        }

    }

    fun saveFolder() {
        Executors.newSingleThreadExecutor().execute {
            folderDAO.insertFolder(Folder(name = folderName!!))
        }

    }

    fun createBookmark(){
        Executors.newSingleThreadExecutor().execute {
            bookmarkDAO.insertBookmark(Bookmark(0,folderID!!,name!!,url!!))
        }
    }

    fun displayToast(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }



}
