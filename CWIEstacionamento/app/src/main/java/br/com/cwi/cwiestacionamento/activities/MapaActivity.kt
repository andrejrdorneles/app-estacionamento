package br.com.cwi.cwiestacionamento.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.view.Gravity
import android.view.MenuItem
import android.widget.ImageView
import br.com.cwi.cwiestacionamento.R
import kotlinx.android.synthetic.main.activity_mapa.*

class MapaActivity : BaseActivity() {

    var mImageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mImageView = findViewById(R.id.imageViewMapa)
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

            R.id.nav_vagas_disponiveis -> {
                intent = Intent(this, VagasDisponiveisActivity::class.java)
            }

            R.id.nav_perfil -> {
                intent = Intent(this, PerfilActivity::class.java)
            }

            R.id.nav_vagas -> {
                intent = Intent(this, VagasActivity::class.java)
            }

            R.id.nav_perfil -> {
                intent = Intent(this, PerfilActivity::class.java)
            }
            else -> { return false }
        }

        mapaDrawerLayout.closeDrawers()
        startActivity(intent)
        return true
    }

    override fun getContentView(): Int {
        return R.layout.activity_mapa
    }

    override fun openDrawers() {
        mapaDrawerLayout.openDrawer(Gravity.START)
    }

    override fun closeDrawers() {
        mapaDrawerLayout.closeDrawers()
    }
}
