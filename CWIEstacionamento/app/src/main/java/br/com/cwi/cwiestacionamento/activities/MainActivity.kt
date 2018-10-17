package br.com.cwi.cwiestacionamento.activities

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.Menu
import android.view.MenuItem
import br.com.cwi.cwiestacionamento.R
import kotlinx.android.synthetic.main.activity_main.*
import br.com.cwi.cwiestacionamento.services.SharedPreferencesService
import br.com.cwi.cwiestacionamento.models.Vaga
import br.com.cwi.cwiestacionamento.utils.PessoaDados
import br.com.cwi.cwiestacionamento.utils.UserHolder
import br.com.cwi.cwiestacionamento.utils.loadImage
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.nav_menu_header.*
import kotlinx.android.synthetic.main.view_navigation.*
import java.util.*

class MainActivity : BaseActivity() {
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var vagasRef: DatabaseReference = database.getReference("0").child("vagas")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var numeroVagas = 0

        vagasRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val list = ArrayList<Vaga>()
                for (vagaSnapshot in p0.children) {
                    val vaga = vagaSnapshot.getValue(Vaga::class.java)!!
                    if (vaga.disponibilidade.equals("disponível")) {
                        numeroVagas.inc()
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

        numeroVagasDisponiveis.text = numeroVagas.toString()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.nav_home -> {
                mainDrawerLayout.closeDrawers()
                return true
            }

            R.id.nav_map -> {
                intent = Intent(this, MapaActivity::class.java)
            }

            R.id.nav_vagas -> {
                intent = Intent(this, VagasActivity::class.java)
            }

            R.id.nav_perfil -> {
                intent = Intent(this, PerfilActivity::class.java)
            }
            else -> { return false }
        }
        closeDrawers()
        startActivity(intent)
        return true
    }

    override fun openDrawers() {
        mainDrawerLayout.openDrawer(GravityCompat.START)
    }

    override fun closeDrawers() {
        mainDrawerLayout.closeDrawers()
    }

    override fun getContentView(): Int {
        return R.layout.activity_main
    }

}
