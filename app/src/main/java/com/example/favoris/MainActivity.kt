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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var folderNameLiveData = MutableLiveData<String>()
    lateinit var getFolderLiveData: LiveData<Folder>


    private lateinit var folderDAO: IFolderDao
    private lateinit var bookmarkDAO: IBookmarkDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getFolderLiveData = Transformations.switchMap(folderNameLiveData){ name ->
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

        }

        //CREATE BOOKMARK
        bookmarkDAO = App.dataBase.bookmarkDAO()
        createBookmarkButton.setOnClickListener {
            folderNameLiveData.value = bookmarkFolderNameEditText.textString()
        }

    }

    private fun createBookmark(id: Long, name: String, url: String) {
//        Executors.newSingleThreadExecutor().execute {//USED A DIFFERENT THREAD
//            bookmarkDAO.insertBookmark(Bookmark(0,id!!,name!!,url!!))
//        }
        CoroutineScope(Default).launch {
            bookmarkDAO.insertBookmark(Bookmark(0,id!!,name!!,url!!))
        }

        bookmarkDAO.getAllBookmarks().observe(this, Observer { bookmarks ->
            bookmarksTextView.text = bookmarks.joinToString ("\n")
        })

        displayToast("Bookmark created successfully")
    }

    fun saveFolder() {
        CoroutineScope(Default).launch {
            folderDAO.insertFolder(Folder(name = createFolderEditText.textString()!!))
        }

//        Executors.newSingleThreadExecutor().execute {//USED A DIFFERENT THREAD
//            folderDAO.insertFolder(Folder(name = createFolderEditText.textString()!!))
//        }
        folderDAO.getFolder(createFolderEditText.text.toString()).observe(this, Observer { folder ->
            Log.i("Main1","folders = $folder")

            if (folder.name == createFolderEditText.textString()) {
                displayToast("Folder created successfully")
            } else {
                displayToast(" WARNING!!! Folder wasn't created ")
            }

        })

    }

    fun displayToast(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    fun EditText.textString(): String {
        return this.text.toString()
    }



}
