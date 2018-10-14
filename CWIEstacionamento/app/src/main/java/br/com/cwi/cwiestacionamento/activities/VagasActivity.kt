package br.com.cwi.cwiestacionamento.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import br.com.cwi.cwiestacionamento.adapters.VagasAdapter
import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot
import br.com.cwi.cwiestacionamento.models.Vaga
import br.com.cwi.cwiestacionamento.R
import br.com.cwi.cwiestacionamento.dialogs.VagaDetalheDialog
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.activity_vagas.*
import kotlinx.android.synthetic.main.view_navigation.*


class VagasActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var myRecyclerView: RecyclerView
    lateinit var vagasAdapter: VagasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vagas)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        navigationView.setNavigationItemSelectedListener(this)

        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var vagasRef: DatabaseReference = database.getReference("0").child("vagas")

        vagasRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val list = ArrayList<Vaga>()
                for (vagaSnapshot in p0.children) {
                    val vaga = vagaSnapshot.getValue(Vaga::class.java)!!
                    list.add(vaga)
                }
                //TODO enviar junto com a lista de vagas sorteadas, a lista de vagas da nova tabela (dia de hoje);
                vagasAdapter = VagasAdapter(list){
                    var vagaDetalheDialog = VagaDetalheDialog()

                    // TODO antes de enviar a vaga para o dialog verificar se existe um registro na nova tabela;

                    vagaDetalheDialog.vaga = it
                    vagaDetalheDialog.show(supportFragmentManager, "tag")
                }
                myRecyclerView = findViewById(R.id.recyclerViewListaVagas)
                myRecyclerView.layoutManager = LinearLayoutManager(this@VagasActivity)
                myRecyclerView.adapter = vagasAdapter
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            vagasDrawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var intent = Intent()
        val fragment: Fragment? = null

        when(item.itemId){

            R.id.nav_home -> {
                intent = Intent(this, MainActivity::class.java)
            }

            R.id.nav_map -> {
                intent = Intent(this, MapaActivity::class.java)
            }

            R.id.nav_vagas -> {
                vagasDrawerLayout.closeDrawers()
                return true
            }

            R.id.nav_perfil -> {
                intent = Intent(this, PerfilActivity::class.java)
            }
            else -> { return false }
        }

        vagasDrawerLayout.closeDrawers()
        startActivity(intent)
        return true
    }
}
