package br.com.cwi.cwiestacionamento.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import br.com.cwi.cwiestacionamento.R
import br.com.cwi.cwiestacionamento.adapters.VagasDisponiveisAdapter
import br.com.cwi.cwiestacionamento.dialogs.VagaDetalheDialog
import br.com.cwi.cwiestacionamento.models.Vaga
import br.com.cwi.cwiestacionamento.services.VagasService
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_vagas.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_navigation.*
import java.text.SimpleDateFormat
import java.util.*

class VagasDisponiveisActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var myRecyclerView: RecyclerView
    lateinit var vagasAdapter: VagasDisponiveisAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vagas_disponiveis)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        navigationView.setNavigationItemSelectedListener(this)

        loadRecyclerViewData()
    }

    private fun loadRecyclerViewData() {

        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var vagasRef: DatabaseReference = database.getReference("0").child("vagas")
        var disponibilidadeVagasRef: DatabaseReference = database.getReference("0").child("disponiveis")

        val listaVagasSorteadas = ArrayList<Vaga>()
        val listaVagasDisponiveis = ArrayList<Vaga>()

        vagasRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (vagaSnapshot in p0.children) {
                    val vaga = vagaSnapshot.getValue(Vaga::class.java)!!
                    vaga.id = vagaSnapshot.key
                    listaVagasSorteadas.add(vaga)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

        disponibilidadeVagasRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                listaVagasDisponiveis.clear()

                for (vagaSnapshot in p0.children) {
                    val vaga = vagaSnapshot.getValue(Vaga::class.java)!!
                    vaga.id = vagaSnapshot.key
                    if (vaga.data.equals(SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time))) {
                        listaVagasDisponiveis.add(vaga)
                    }
                }

                vagasAdapter = VagasDisponiveisAdapter(listaVagasDisponiveis) {
                    var vagaDetalheDialog = VagaDetalheDialog()

                    if (VagasService().vagaEstaDisponivel(listaVagasDisponiveis, it)) {
                        vagaDetalheDialog.vaga = listaVagasDisponiveis.stream()
                                .filter { v ->
                                    v.vaga!! == it.vaga && v.disponibilidade.equals("disponível")
                                }.findFirst().get()
                    } else {
                        vagaDetalheDialog.vaga = it
                    }

                    if (VagasService().buscarVagaDoUsuario(listaVagasSorteadas, listaVagasDisponiveis) != null) {
                        vagaDetalheDialog.possuiVaga = true
                        vagaDetalheDialog.vagaDoUsuario = VagasService().buscarVagaDoUsuario(listaVagasSorteadas, listaVagasDisponiveis)!!
                    }
                    vagaDetalheDialog.show(supportFragmentManager, "tag")
                }

                myRecyclerView = findViewById(R.id.recyclerViewListaVagas)
                myRecyclerView.layoutManager = LinearLayoutManager(this@VagasDisponiveisActivity)
                myRecyclerView.adapter = vagasAdapter
                vagasAdapter.notifyDataSetChanged()
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
        when (item.itemId) {

            R.id.nav_home -> {
                intent = Intent(this, MainActivity::class.java)
            }

            R.id.nav_vagas_disponiveis -> {
                vagasDrawerLayout.closeDrawers()
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
            else -> {
                return false
            }
        }
        vagasDrawerLayout.closeDrawers()
        startActivity(intent)
        return true
    }
}