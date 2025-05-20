package com.example.myapplication.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.databinding.ActivityWorkDetailsBinding

class WorkDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityWorkDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.approveGrn.text = intent.getStringExtra("routineName")

    }
}