package com.azarasi.mp3player.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.azarasi.mp3player.R
import com.azarasi.mp3player.ViewModelFactory.getViewModel
import com.azarasi.mp3player.adapter.PlayListFilesAdapter

@SuppressLint("NotifyDataSetChanged")
class PlayListFilesFragment(private val playListId: Long) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_playlistfiles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.adapter = PlayListFilesAdapter(requireActivity(), playListId)

        val addButton: ImageView = view.findViewById(R.id.addButton)
        addButton.setOnClickListener {
            val editText = EditText(requireContext())
            editText.hint = "ファイルのパス"
            val dialog = AlertDialog.Builder(requireContext())
            dialog.setTitle("MP3を追加")
            dialog.setView(editText)
            dialog.setPositiveButton("追加", DialogInterface.OnClickListener { _, _ ->
                if (editText.text.isNullOrEmpty())
                    return@OnClickListener
                val currentPlayList = getViewModel().findPlayListById(playListId)!!
                getViewModel().addPlayListFile(currentPlayList, editText.text.toString())
                getViewModel().savePlayLists()
            })
            dialog.setNegativeButton("閉じる", null)
            dialog.show()
        }
    }
}