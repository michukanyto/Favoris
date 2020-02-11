package com.example.favoris.viewmodel


import androidx.lifecycle.*
import com.example.favoris.App
import com.example.favoris.dao.IBookmarkDao
import com.example.favoris.dao.IFolderDao
import com.example.favoris.model.Bookmark
import com.example.favoris.model.Folder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch

class ViewModelState(val success:Boolean = false)

class FavoriViewModel : ViewModel() {

    private val folderDAO: IFolderDao = App.dataBase.folderDAO()
    private val bookmarkDAO: IBookmarkDao = App.dataBase.bookmarkDAO()
    private lateinit var folderLiveData: LiveData<Boolean>

    fun saveFolder(folderName: String) {
        CoroutineScope(Default).launch {
            folderDAO.insertFolder(Folder(name = folderName))
        }

        folderLiveData = Transformations.map(folderDAO.getFolder(folderName)){
            folderName == it.name
        }
    }


    fun getState() : LiveData<Boolean> = folderLiveData

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


}