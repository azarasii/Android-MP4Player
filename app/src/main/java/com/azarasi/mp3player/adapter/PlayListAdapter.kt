package com.azarasi.mp3player.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.azarasi.mp3player.PlayList
import com.azarasi.mp3player.R
import com.azarasi.mp3player.ViewModelFactory.getViewModel
import com.azarasi.mp3player.fragment.PlayListFilesFragment

class PlayListAdapter(private val activity: FragmentActivity) : RecyclerView.Adapter<PlayListAdapter.ViewHolder>() {
    private var playLists: ArrayList<PlayList> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    private val observer = Observer<ArrayList<PlayList>> { playLists ->
        this.playLists = playLists
        notifyDataSetChanged()
    }

    init {
        getViewModel().playListLiveData.observe(activity, observer)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.name)
        val actionBtn = itemView.findViewById<ImageView>(R.id.actionBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(parent.context, R.layout.item_playlist, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playList = playLists[position]
        holder.name.text = playList.name
        holder.actionBtn.setOnClickListener {
            val dialog = AlertDialog.Builder(activity)
            dialog.setTitle(playList.name)
            dialog.setMessage("削除しますか？")
            dialog.setPositiveButton("はい") { _, _ ->
                getViewModel().deletePlayList(playList)
                getViewModel().savePlayLists()
            }
            dialog.setNegativeButton("閉じる", null)
            dialog.show()
        }
        holder.itemView.setOnClickListener {
            val fragmentManager = activity.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack("playlists")
            fragmentTransaction.replace(R.id.fragment_container, PlayListFilesFragment(playList.id))
            fragmentTransaction.commit()
        }
    }

    override fun getItemCount(): Int {
        return playLists.size
    }
}