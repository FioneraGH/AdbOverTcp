package com.fionera.adbtcp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * BaseActivity
 * Created by fionera on 17-3-10 in android_project.
 */

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    protected lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
    }
}
