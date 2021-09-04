package com.example.lukyanovpavel.utils

enum class Pages {
    TOP,
    LATEST,
    HOT
}

object Page {

    fun loadPage(page: Pages): String {
        return when (page) {
            Pages.TOP -> "top"
            Pages.LATEST -> "latest"
            Pages.HOT -> "hot"
        }
    }
}