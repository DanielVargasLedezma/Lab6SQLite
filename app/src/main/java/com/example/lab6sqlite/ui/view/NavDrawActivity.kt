package com.example.lab6sqlite.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.lab6sqlite.R
import com.example.lab6sqlite.databinding.ActivityNavDrawBinding
import com.example.lab6sqlite.ui.view.fragment.estudiante.EstudiantesFragment

class NavDrawActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityNavDrawBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavDrawBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        binding.apply {
            toggle = ActionBarDrawerToggle(
                this@NavDrawActivity, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
            )

            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            navView.setNavigationItemSelectedListener(this@NavDrawActivity)
        }
    }

    private fun setupToolbar() {
        /*
         * La primera línea se puede quitar y pondría la toolbar por defecto que si le funcionan bien los íconos,
         * pero hace que la nav se abra por atrás entonces como que no. Se le quita el título y permite que se muestre
         * lo que viene siendo el toggle con los últimos dos métodos.
         */
        setSupportActionBar(binding.appBarNavDraw.toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun replaceFragments(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.appBarNavDraw.contentMain.id, fragment)
        fragmentTransaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                binding.drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true

        binding.drawerLayout.closeDrawers()

        when (item.itemId) {
            R.id.estudiantes -> {
                supportActionBar?.title = "Estudiantes Registrados"

                replaceFragments(
                    EstudiantesFragment.newInstance()
                )
            }
            R.id.cursos -> {
                supportActionBar?.title = "Cursos Registrados"
            }
            R.id.logout -> {
                finish()
                startActivity(Intent(this@NavDrawActivity, MainActivity::class.java))
            }
        }

        return true
    }
}