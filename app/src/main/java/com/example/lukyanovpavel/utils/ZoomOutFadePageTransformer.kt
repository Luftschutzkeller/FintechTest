package com.example.lukyanovpavel.utils

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs
import kotlin.math.max

private const val MIN_ALPHA = 0.6f
private const val MIN_SCALE = 0.85f

class ZoomOutFadePageTransformer(
    private val pageTranslationWidth: Int
) : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val scaleFactor = max(MIN_SCALE, 1 - abs(position))
        val alphaFactor = max(MIN_ALPHA, 1 - abs(position))

        val pageScaleTransformWidth = pageWidth * (1 - scaleFactor) / 2
        page.translationX = -(pageTranslationWidth + pageScaleTransformWidth) * position

        page.scaleX = scaleFactor
        page.scaleY = scaleFactor

        page.alpha = alphaFactor
    }
}