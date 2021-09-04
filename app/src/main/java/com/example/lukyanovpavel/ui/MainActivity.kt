package com.example.lukyanovpavel.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lukyanovpavel.R
import com.example.lukyanovpavel.ui.posts.ScreenContainer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, ScreenContainer.newInstance())
                .commit()
        }
    }
}