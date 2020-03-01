package com.example.favoris.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.*
import com.example.favoris.R
import com.example.favoris.model.Folder
import com.example.favoris.viewmodel.FavoriViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var folderNameLiveData = MutableLiveData<String>()
    lateinit var getFolderLiveData: LiveData<Folder>
    private lateinit var viewModel: FavoriViewModel
    lateinit var folder: Folder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        liveDataOwner = this
        viewModel = ViewModelProviders.of(this).get(FavoriViewModel::class.java)

        //switchMap return a LiveData map return a value
        getFolderLiveData = Transformations.switchMap(folderNameLiveData){ name ->
            viewModel.getFolder(name)
        }

        getFolderLiveData.observe(this, Observer { folder ->
            viewModel.createBookMark(folder!!.id,bookmarkFolderNameEditText.textString(),bookmarkUrlEditText.textString())
            displayToast("Bookmark created successfully")
            viewModel.getBookmarks().observe(this, Observer {bookmarks ->
                bookmarksTextView.text = bookmarks.joinToString ("\n")

            })

        })

        //CREATE FOLDER
        createFolderButton.setOnClickListener {
            viewModel.saveFolder(createFolderEditText.textString())

            viewModel.getState().observe(this, Observer {
                if (it) displayToast("Folder created successfully") else displayToast(" WARNING!!! Folder wasn't created ")
            })
            bookmarkFolderNameEditText.setText(createFolderEditText.textString())
        }

        //CREATE BOOKMARK
        createBookmarkButton.setOnClickListener {
            folderNameLiveData.value = bookmarkFolderNameEditText.textString()
        }
    }

    fun displayToast(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    fun EditText.textString(): String {
        return this.text.toString()
    }



}
