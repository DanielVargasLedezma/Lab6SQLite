package com.example.lab6sqlite.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.lab6sqlite.database.AdminDataBaseHelper
import com.example.lab6sqlite.databinding.ActivityCrearUsuarioBinding

class CrearUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrearUsuarioBinding

    private val adminDataBaseHelper = AdminDataBaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCrearUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            insertarUsuario.setOnClickListener {
                textErrorCamposVacios.visibility = View.GONE
                textErrorUsurioYaCreado.visibility = View.GONE

                if (usernameNew.text.isNotEmpty() && passwordNew.text.isNotEmpty()) {
                    if (adminDataBaseHelper.insertAdmin(
                            usernameNew.text.toString(),
                            passwordNew.text.toString()
                        ) != -1L
                    ) {
                        Toast.makeText(this@CrearUsuarioActivity, "Usuario creado con éxito.", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        textErrorUsurioYaCreado.visibility = View.VISIBLE
                    }
                } else {
                    textErrorCamposVacios.visibility = View.VISIBLE
                }
            }

            irAtras.setOnClickListener {
                finish()
            }
        }
    }
}