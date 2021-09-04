package com.example.lukyanovpavel.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.lukyanovpavel.R

fun ImageView.load(
    url: Any?
) {
    Glide.with(this)
        .asGif()
        .load(url)
        .placeholder(R.drawable.loading_placeholder)
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .into(this)
}