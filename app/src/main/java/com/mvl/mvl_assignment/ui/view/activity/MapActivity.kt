package com.mvl.mvl_assignment.ui.view.activity

import android.os.Bundle
import androidx.navigation.findNavController

import com.mvl.mvl_assignment.R
import com.mvl.mvl_assignment.databinding.ActivityMapBinding
import com.mvl.mvl_assignment.ui.base.BaseActivity
import com.mvl.mvl_assignment.ui.view.fragments.RequestBookFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMapBinding.inflate(layoutInflater).root)
    }




}