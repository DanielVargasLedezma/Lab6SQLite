package com.example.lab6sqlite.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.lab6sqlite.database.AdminDataBaseHelper
import com.example.lab6sqlite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val adminDataBaseHelper = AdminDataBaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            val intent = Intent(this@MainActivity, NavDrawActivity::class.java)

            crearUsuario.setOnClickListener {
                startActivity(Intent(this@MainActivity, CrearUsuarioActivity::class.java))
            }

            loginButton.setOnClickListener {
                errorLogin.visibility = View.GONE

                if (adminDataBaseHelper.doLogin(usuarioALoggear.text.toString(), passALoggear.text.toString())) {
                    finish()
                    startActivity(intent)
                } else errorLogin.visibility = View.VISIBLE
            }
        }
    }
}