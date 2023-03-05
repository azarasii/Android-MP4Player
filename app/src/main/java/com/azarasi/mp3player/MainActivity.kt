package com.azarasi.mp3player

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.azarasi.mp3player.ViewModelFactory.getViewModel
import com.azarasi.mp3player.fragment.PlayListsFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentContainer: FrameLayout

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentContainer = findViewById(R.id.fragment_container)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val fragment = PlayListsFragment()
        fragmentTransaction.add(R.id.fragment_container, fragment)
        fragmentTransaction.commit()

        onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    supportFragmentManager.popBackStack()
                }
            })
    }

    override fun onDestroy() {
        getViewModel().savePlayLists()
        super.onDestroy()
    }
}