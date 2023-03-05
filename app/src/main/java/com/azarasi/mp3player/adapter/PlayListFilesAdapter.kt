package com.azarasi.mp3player.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.azarasi.mp3player.PlayList
import com.azarasi.mp3player.R
import com.azarasi.mp3player.ViewModelFactory

@SuppressLint("NotifyDataSetChanged")
class PlayListFilesAdapter(activity: FragmentActivity, private val playListId: Long) : RecyclerView.Adapter<PlayListFilesAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filePath = itemView.findViewById<TextView>(R.id.filePath)
    }

    private lateinit var playList: PlayList

    init {
        ViewModelFactory.getViewModel().playListLiveData.observe(activity) { it ->
            it.forEach {
                if (it.id == playListId) {
                    playList = it
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(parent.context, R.layout.item_musicfile, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filePath = playList.filePaths[position]
        holder.filePath.text = filePath
    }

    override fun getItemCount(): Int {
        return playList.filePaths.size
    }
}