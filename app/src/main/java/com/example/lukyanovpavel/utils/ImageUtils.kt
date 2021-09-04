package com.example.lukyanovpavel.utils

import android.graphics.drawable.AnimationDrawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

fun ImageView.load(
    url: Any?,
    animLoading: AnimationDrawable
) {
    Glide.with(this)
        .asGif()
        .load(url)
        .placeholder(animLoading)
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .into(this)
}