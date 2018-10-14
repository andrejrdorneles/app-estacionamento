package br.com.cwi.cwiestacionamento.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import br.com.cwi.cwiestacionamento.R
import br.com.cwi.cwiestacionamento.adapters.VagasAdapter
import br.com.cwi.cwiestacionamento.dialogs.VagaDetalheDialog
import br.com.cwi.cwiestacionamento.models.Vaga
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_navigation.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var vagasRef: DatabaseReference = database.getReference("0").child("vagas")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            mainDrawerLayout.openDrawer(GravityCompat.START)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var intent = Intent()
        val fragment: Fragment? = null

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

        mainDrawerLayout.closeDrawers()
        startActivity(intent)
        return true
    }
}
