package com.example.favoris.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.favoris.App
import com.example.favoris.dao.IBookmarkDao
import com.example.favoris.dao.IFolderDao
import com.example.favoris.model.Folder
import com.example.favoris.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import kotlin.random.Random

class ViewModelState(val success:Boolean = false)

class FavoriViewModel : ViewModel() {

    private lateinit var folderDAO: IFolderDao
    private lateinit var bookmarkDAO: IBookmarkDao

    fun saveFolder(folderName: String) {
        folderDAO = App.dataBase.folderDAO()
        CoroutineScope(Default).launch {
            folderDAO.insertFolder(Folder(name = folderName))
        }

        folderDAO.getFolder(folderName).observe(MainActivity.liveDataOwner, Observer {folder ->
            if (folderName == folder.name) {
                success.value = ViewModelState(true)
            } else {
                success.value = ViewModelState(false)
            }
        })
    }

    private val success = MutableLiveData<ViewModelState>()

    fun getState(): LiveData<ViewModelState> = success


}