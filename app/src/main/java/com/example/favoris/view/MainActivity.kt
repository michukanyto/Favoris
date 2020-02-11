package com.example.favoris.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.*
import com.example.favoris.App
import com.example.favoris.R
import com.example.favoris.dao.IBookmarkDao
import com.example.favoris.dao.IFolderDao
import com.example.favoris.model.Bookmark
import com.example.favoris.model.Folder
import com.example.favoris.viewmodel.FavoriViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var liveDataOwner: LifecycleOwner
    }

    private var folderNameLiveData = MutableLiveData<String>()
    lateinit var getFolderLiveData: LiveData<Folder>
    private lateinit var viewModel: FavoriViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        liveDataOwner = this
        viewModel = ViewModelProviders.of(this).get(FavoriViewModel::class.java)

        getFolderLiveData = Transformations.switchMap(folderNameLiveData){ name ->
//            folderDAO.getFolder(name)
            viewModel.getFolder(name)
        }

        getFolderLiveData.observe(this, Observer { folder ->
//            createBookmark(folder!!.id,bookmarkFolderNameEditText.textString(),bookmarkUrlEditText.textString())
            viewModel.createBookMark(folder!!.id,bookmarkFolderNameEditText.textString(),bookmarkUrlEditText.textString())
            displayToast("Bookmark created successfully")
            viewModel.getBookmarks().observe(this, Observer {bookmarks ->
                bookmarksTextView.text = bookmarks.joinToString ("\n")

            })

        })


        //CREATE FOLDER
        createFolderButton.setOnClickListener {
            viewModel.saveFolder(createFolderEditText.textString()!!)
//            /////////première solution
//            viewModel.getState().observe(this, Observer { state ->
            /////////sln with transformation
            viewModel.getState2().observe(this, Observer {
                if (it) {
                    displayToast("Folder created successfully")
                } else {
                    displayToast(" WARNING!!! Folder wasn't created ")
                }
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
