package com.erif.textviewquery.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.erif.textviewquery.databinding.ActivityAnotherExampleBinding

class AnotherExample : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAnotherExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}