package br.com.cwi.cwiestacionamento.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import android.widget.ImageView
import br.com.cwi.cwiestacionamento.R
import kotlinx.android.synthetic.main.activity_mapa.*
import kotlinx.android.synthetic.main.activity_vagas.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_navigation.*

class MapaActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var mImageView: ImageView? = null
//    var mAttacher: PhotoViewAttacher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        mImageView = findViewById(R.id.imageViewMapa)

        // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
//        mAttacher = new PhotoViewAttacher(mImageView)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            mapaDrawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var intent = Intent()

        when(item.itemId){

            R.id.nav_home -> {
                intent = Intent(this, MainActivity::class.java)
            }

            R.id.nav_map -> {
                mapaDrawerLayout.closeDrawers()
                return true
            }

            R.id.nav_vagas -> {
                intent = Intent(this, VagasActivity::class.java)
            }
            else -> { return false }
        }

        mapaDrawerLayout.closeDrawers()
        startActivity(intent)
        return true
    }
}
