package com.linkdevelpment.newsfeeds.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import com.linkdevelpment.newsfeeds.R
import com.linkdevelpment.newsfeeds.core.presentation.base.BaseActivity
import com.linkdevelpment.newsfeeds.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setListenersOnViews()
    }

    private fun setListenersOnViews() {
        binding.ivMenu.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        binding.slideLayout.ivArrow.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        binding.slideLayout.layoutExplore.setOnClickListener {
            Toasty.success(this, getString(R.string.explore), Toast.LENGTH_SHORT, true).show()
            binding.slideLayout.ivSelectedExplore.isVisible = true
            binding.slideLayout.ivSelectedChat.isVisible = false
            binding.slideLayout.ivSelectedGallery.isVisible = false
            binding.slideLayout.ivSelectedMagazine.isVisible = false
            binding.slideLayout.ivSelectedWishList.isVisible = false
        }

        binding.slideLayout.layoutLiveChat.setOnClickListener {
            Toasty.success(this, getString(R.string.live_chat), Toast.LENGTH_SHORT, true).show()
            binding.slideLayout.ivSelectedChat.isVisible = true
            binding.slideLayout.ivSelectedGallery.isVisible = false
            binding.slideLayout.ivSelectedMagazine.isVisible = false
            binding.slideLayout.ivSelectedWishList.isVisible = false
            binding.slideLayout.ivSelectedExplore.isVisible = false
        }
        binding.slideLayout.layoutGallery.setOnClickListener {
            Toasty.success(this, getString(R.string.gallery), Toast.LENGTH_SHORT, true).show()
            binding.slideLayout.ivSelectedGallery.isVisible = true
            binding.slideLayout.ivSelectedMagazine.isVisible = false
            binding.slideLayout.ivSelectedWishList.isVisible = false
            binding.slideLayout.ivSelectedChat.isVisible = false
            binding.slideLayout.ivSelectedExplore.isVisible = false
        }
        binding.slideLayout.layoutWishList.setOnClickListener {
            Toasty.success(this, getString(R.string.wishlist), Toast.LENGTH_SHORT, true).show()
            binding.slideLayout.ivSelectedWishList.isVisible = true
            binding.slideLayout.ivSelectedMagazine.isVisible = false
            binding.slideLayout.ivSelectedGallery.isVisible = false
            binding.slideLayout.ivSelectedChat.isVisible = false
            binding.slideLayout.ivSelectedExplore.isVisible = false
        }
        binding.slideLayout.layoutEMagazine.setOnClickListener {
            Toasty.success(this, getString(R.string.emagazine), Toast.LENGTH_SHORT, true).show()
            binding.slideLayout.ivSelectedMagazine.isVisible = true
            binding.slideLayout.ivSelectedWishList.isVisible = false
            binding.slideLayout.ivSelectedGallery.isVisible = false
            binding.slideLayout.ivSelectedChat.isVisible = false
            binding.slideLayout.ivSelectedExplore.isVisible = false

        }


    }


}