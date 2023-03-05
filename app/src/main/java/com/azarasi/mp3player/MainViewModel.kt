package com.azarasi.mp3player

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.azarasi.mp3player.MyApp.Companion.myApp
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.io.File
import java.util.*


data class PlayList(
    val id: Long,
    val name: String,
    val filePaths: ArrayList<String> = ArrayList()
) {
    companion object {
        private fun randomId(): Long = Random().nextLong()

        fun createPlayList(playListName: String): PlayList {
            return PlayList(id = randomId(), name = playListName, filePaths = ArrayList())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        return this.id == (other as PlayList).id
    }
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("data", Context.MODE_PRIVATE)
    private val edit = sharedPreferences.edit()
    var gson = Gson()

    private val KEY_PLAYLISTS = "playlists"
    private var mPlayLists: MutableLiveData<ArrayList<PlayList>> = MutableLiveData<ArrayList<PlayList>>()
    val playListLiveData: LiveData<ArrayList<PlayList>> = mPlayLists

    init {
        mPlayLists.value = loadPlayLists()
    }

    fun getPlayLists(): ArrayList<PlayList> {
        return mPlayLists.value ?: ArrayList()
    }

    private fun loadPlayLists(): ArrayList<PlayList> {
        val data: String? = sharedPreferences.getString(KEY_PLAYLISTS, null)
        if (data.isNullOrEmpty())
            return ArrayList()
        return try {
            gson.fromJson(data, object : TypeToken<ArrayList<PlayList>>() {}.type)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            edit.remove(KEY_PLAYLISTS)
            ArrayList()
        }
    }

    fun findPlayListById(id: Long): PlayList? {
        mPlayLists.value?.forEach {
            if (it.id == id)
                return it
        }
        return null
    }

    fun savePlayLists() {
        val data = gson.toJson(mPlayLists.value)
        edit.putString(KEY_PLAYLISTS, data).commit()
    }

    fun createNewPlayList(playListName: String) {
        val newPlayList = PlayList.createPlayList(playListName)
        mPlayLists.value = getPlayLists().apply { add(newPlayList) }
    }

    fun deletePlayList(playList: PlayList) {
        mPlayLists.value = getPlayLists().apply { remove(playList) }
    }

    fun existPlayListFile(playList: PlayList, filePath: String): Boolean {
        return playList.filePaths.contains(filePath)
    }

    fun addPlayListFile(playList: PlayList, filePath: String) {
        if (!File(filePath).exists() || !File(filePath).isFile || !File(filePath).name.lowercase(Locale.ROOT).endsWith(".mp3"))
            return
        if (existPlayListFile(playList, filePath)) return
        mPlayLists.value?.forEach {
            if (it.name == playList.name) {
                it.filePaths.add(filePath)
                return@forEach
            }
        }
    }
}

object ViewModelFactory {
    private val viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(myApp).create(MainViewModel::class.java)

    fun getViewModel(): MainViewModel {
        return viewModel
    }
}