package com.example.favoris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.favoris.model.Folder
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private var folderName:String? = null
    private var bookmark:String? = null
    private var name:String? = null
    private var url:String? = null

    val folderDAO = App.dataBase.folderDAO()
    val bookmarkDAO = App.dataBase.bookmarkDAO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        createFolderButton.setOnClickListener {
            folderName = createFolderEditText.text.toString()
            saveFolder()
            bookmarkFolderNameEditText.setText(folderName)
            folderDAO.getFolder(folderName!!).observe(this, Observer { folder ->
                Log.i("Main1","folders = $folder")
                if (folder.name == folderName) {
                    displayToast("Folder created successfully")
                } else {
                    displayToast(" WARNING!!! Folder wasn't created ")
                }

            })

        }

        createBookmarkButton.setOnClickListener {
            bookmark = bookmarkFolderNameEditText.text.toString()
            name = bookmarkNameEditText.text.toString()
            url = bookmarkUrlEditText.text.toString()
        }

    }

    fun saveFolder() {
        Executors.newSingleThreadExecutor().execute {
            folderDAO.insertFolder(Folder(0, folderName!!))
        }

    }

    fun displayToast(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }



}
