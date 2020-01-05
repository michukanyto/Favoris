package com.example.favoris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.example.favoris.dao.IBookmarkDao
import com.example.favoris.dao.IFolderDao
import com.example.favoris.model.Bookmark
import com.example.favoris.model.Folder
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private var folderName = MutableLiveData<String>()
    lateinit var getFolderLiveData: LiveData<Folder>
    private var name:String? = null
    private var url:String? = null




    private lateinit var folderDAO: IFolderDao
    private lateinit var bookmarkDAO: IBookmarkDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getFolderLiveData = Transformations.switchMap(folderName){name ->
            folderDAO.getFolder(name)
        }

        getFolderLiveData.observe(this, Observer { folder ->
            createBookmark(folder!!.id,bookmarkFolderNameEditText.textString(),bookmarkUrlEditText.textString())
        })


        //CREATE FOLDER
        folderDAO = App.dataBase.folderDAO()
        createFolderButton.setOnClickListener {
            saveFolder()
            bookmarkFolderNameEditText.setText(createFolderEditText.textString())
            folderDAO.getFolder(createFolderEditText.text.toString()!!).observe(this, Observer { folder ->
                Log.i("Main1","folders = $folder")

                if (folder.name == createFolderEditText.textString()) {
                    displayToast("Folder created successfully")
                } else {
                    displayToast(" WARNING!!! Folder wasn't created ")
                }

            })
        }

        //CREATE BOOKMARK
        bookmarkDAO = App.dataBase.bookmarkDAO()
        createBookmarkButton.setOnClickListener {
            folderName.value = bookmarkFolderNameEditText.textString()
        }

    }

    private fun createBookmark(id: Long, name: String, url: String) {
        Executors.newSingleThreadExecutor().execute {
            bookmarkDAO.insertBookmark(Bookmark(0,id!!,name!!,url!!))
        }

        bookmarkDAO.getAllBookmarks().observe(this, Observer { bookmarks ->
            bookmarksTextView.text = bookmarks.toString()
        })
    }

    fun saveFolder() {
        Executors.newSingleThreadExecutor().execute {
            folderDAO.insertFolder(Folder(name = createFolderEditText.textString()!!))
        }

    }

//    fun createBookmark(){
//        Executors.newSingleThreadExecutor().execute {
//            bookmarkDAO.insertBookmark(Bookmark(0,folderID!!,name!!,url!!))
//        }
//    }

    fun displayToast(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    fun EditText.textString(): String {
        return this.text.toString()
    }



}
