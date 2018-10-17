package br.com.cwi.cwiestacionamento.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.view.Menu
import android.view.MenuItem
import br.com.cwi.cwiestacionamento.R
import br.com.cwi.cwiestacionamento.services.SharedPreferencesService
import br.com.cwi.cwiestacionamento.utils.PessoaDados
import br.com.cwi.cwiestacionamento.utils.UserHolder
import br.com.cwi.cwiestacionamento.utils.loadImage
import kotlinx.android.synthetic.main.nav_menu_header.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_navigation.*

abstract class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        navigationView.setNavigationItemSelectedListener(this)
    }

    abstract fun getContentView() : Int

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        updateInfoUser()
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onRestart() {
        super.onRestart()
        updateInfoUser()
    }

    private fun updateInfoUser() {
        menuHeaderPersonEmail.text = SharedPreferencesService.retrieveString(PessoaDados.EMAIL.value)
        menuHeaderPersonName.text = SharedPreferencesService.retrieveString(PessoaDados.NAME.value)
        val uriProfileImage = SharedPreferencesService.retrieveString(PessoaDados.IMAGE.value)

        if (!uriProfileImage.isNullOrBlank()) {
            personImageHeaderMenu.loadImage(uriProfileImage)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            openDrawers()
            return true
        }

        if (item?.itemId == R.id.logout) {
            UserHolder.logOut(this)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    abstract fun openDrawers()

    abstract fun closeDrawers()
}
