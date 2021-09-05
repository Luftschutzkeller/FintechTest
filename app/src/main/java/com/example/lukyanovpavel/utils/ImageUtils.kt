package com.example.lukyanovpavel.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import timber.log.Timber

fun ImageView.load(
    url: Any?
) {
    Timber.tag("ttt").d("Glide - $url")
    Glide.with(this)
        .asGif()
        .load(url)
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .into(this)
}