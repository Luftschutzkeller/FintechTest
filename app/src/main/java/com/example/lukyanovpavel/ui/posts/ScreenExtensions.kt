package com.example.lukyanovpavel.ui.posts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.widget.ViewPager2
import com.example.lukyanovpavel.R
import com.example.lukyanovpavel.databinding.LayoutErrorBinding
import com.example.lukyanovpavel.databinding.LayoutPagerBinding
import com.example.lukyanovpavel.databinding.LayoutPostBinding
import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.ui.adapters.ViewPagerAdapter
import com.example.lukyanovpavel.ui.posts.hot.ScreenHot
import com.example.lukyanovpavel.ui.posts.latest.ScreenLatest
import com.example.lukyanovpavel.ui.posts.top.ScreenTop
import com.example.lukyanovpavel.utils.ZoomOutFadePageTransformer
import com.example.lukyanovpavel.utils.load
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

fun ViewPager2.bind(
    tabLayout: TabLayout,
    context: Context,
    layoutInflater: LayoutInflater,
    childFragmentManager: FragmentManager,
    lifecycle: Lifecycle
) {
    val category = listOf(
        context.getString(R.string.category_latest),
        context.getString(R.string.category_top),
        context.getString(R.string.category_hot)
    )
    val fragmentList = listOf(
        ScreenLatest(),
        ScreenTop(),
        ScreenHot()
    )

    adapter = ViewPagerAdapter(
        fragmentList,
        childFragmentManager,
        lifecycle
    )
    setPageTransformer()
    offscreenPageLimit = fragmentList.size
    tabLayout.bind(
        context,
        this,
        layoutInflater,
        category
    )
}

private fun ViewPager2.setPageTransformer() {
    val nextCardVisibleWidth =
        resources.getDimensionPixelSize(R.dimen.nex_item_visible)
    val cardInnerMargin = resources.getDimensionPixelSize(R.dimen.item_inner_margin)
    val pageTranslation = nextCardVisibleWidth + cardInnerMargin
    setPageTransformer(ZoomOutFadePageTransformer(pageTranslation))
}

private fun TabLayout.bind(
    context: Context,
    pager: ViewPager2,
    layoutInflater: LayoutInflater,
    category: List<String>
) {
    this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {

        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            (tab?.customView?.findViewById(R.id.tab_item) as TextView).setTextColor(
                context.getColor(R.color.tab_color)
            )
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            (tab?.customView?.findViewById(R.id.tab_item) as TextView).setTextColor(
                context.getColor(R.color.main_text_color)
            )
        }

    })

    TabLayoutMediator(this, pager,
        TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            val inflate =
                layoutInflater.inflate(R.layout.item_tab, this, false)
            tab.customView = inflate
            inflate.findViewById<TextView>(R.id.tab_item).text = category[position]
        }).attach()
}

fun LayoutPagerBinding.bind(
    tabLayout: TabLayout,
    context: Context,
    layoutInflater: LayoutInflater,
    childFragmentManager: FragmentManager,
    lifecycle: Lifecycle
) {
    pager.bind(
        tabLayout, context, layoutInflater, childFragmentManager, lifecycle
    )
}

fun LayoutErrorBinding.bind(
    error: Throwable?
) {
    errorText.text = error?.localizedMessage
}

fun LayoutPostBinding.bind(
    post: Any?
) {
    when (post) {
        is Post -> {
            postImage.load(post.gifURL)
            postDescription.text = post.description
        }
    }
}

fun View.disableClickTemporarily() {
    isClickable = false
    postDelayed({
        isClickable = true
    }, 1500)
}