package com.example.favoris.viewmodel

import android.util.Log
import android.view.animation.Transformation
import androidx.lifecycle.*
import com.example.favoris.App
import com.example.favoris.dao.IBookmarkDao
import com.example.favoris.dao.IFolderDao
import com.example.favoris.model.Bookmark
import com.example.favoris.model.Folder
import com.example.favoris.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import kotlin.random.Random

class ViewModelState(val success:Boolean = false)

class FavoriViewModel : ViewModel() {

    private val folderDAO: IFolderDao = App.dataBase.folderDAO()
    private val bookmarkDAO: IBookmarkDao = App.dataBase.bookmarkDAO()
    /////////sln
    private lateinit var folderLiveData: LiveData<Boolean>
    /////////sln 2
//    private val lifecycleOwner: LifecycleOwner? = context as? LyfecycleOwner

    fun saveFolder(folderName: String) {
        CoroutineScope(Default).launch {
            folderDAO.insertFolder(Folder(name = folderName))
        }

/////////sln
        folderLiveData = Transformations.map(folderDAO.getFolder(folderName)){
            folderwasCreated(folderName, it)
        }

        //première solution
//        folderDAO.getFolder(folderName).observe(MainActivity.liveDataOwner, Observer {folder ->
//            if (folderName == folder.name) {
//                success.value = ViewModelState(true)
//            } else {
//                success.value = ViewModelState(false)
//            }
//        })
    }

    private val success = MutableLiveData<ViewModelState>()

    //première solution
//    fun getState(): LiveData<ViewModelState> = success

    fun getState2() : LiveData<Boolean> = folderLiveData

    fun createBookMark(folderId: Long,bmName: String,bmUrl: String) {
        CoroutineScope(Default).launch {
            bookmarkDAO.insertBookmark(Bookmark(0,folderId,bmName,bmUrl))
        }
    }

    fun getFolder(name: String) : LiveData<Folder> {
        return folderDAO.getFolder(name)
    }

    fun getBookmarks() : LiveData<List<Bookmark>> {

        return bookmarkDAO.getAllBookmarks()
    }

    /////////sln
    fun folderwasCreated(folderName:String, folder: Folder) : Boolean {

        if (folderName == folder.name) {
            success.value = ViewModelState(true)
            return true

        } else {
            success.value = ViewModelState(false)
            return false
        }
    }


}