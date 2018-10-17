package br.com.cwi.cwiestacionamento.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import br.com.cwi.cwiestacionamento.adapters.VagasAdapter
import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot
import br.com.cwi.cwiestacionamento.models.Vaga
import br.com.cwi.cwiestacionamento.R
import br.com.cwi.cwiestacionamento.dialogs.VagaDetalheDialog
import br.com.cwi.cwiestacionamento.services.SharedPreferencesService
import br.com.cwi.cwiestacionamento.utils.PessoaDados
import br.com.cwi.cwiestacionamento.utils.loadImage
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.activity_vagas.*
import kotlinx.android.synthetic.main.nav_menu_header.*
import kotlinx.android.synthetic.main.view_navigation.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class VagasActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var currentUser = FirebaseAuth.getInstance().currentUser

    lateinit var myRecyclerView: RecyclerView
    lateinit var vagasAdapter: VagasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vagas)

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

        disponibilidadeVagasRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (vagaSnapshot in p0.children) {
                    val vaga = vagaSnapshot.getValue(Vaga::class.java)!!
                    vaga.id = vagaSnapshot.key
                    if (vaga.data.equals(SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time))) {
                        listaVagasDisponiveis.add(vaga)
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })

        vagasRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (vagaSnapshot in p0.children) {
                    val vaga = vagaSnapshot.getValue(Vaga::class.java)!!
                    vaga.id = vagaSnapshot.key
                    listaVagasSorteadas.add(vaga)
                }

                vagasAdapter = VagasAdapter(listaVagasSorteadas, listaVagasDisponiveis) {
                    var vagaDetalheDialog = VagaDetalheDialog()

                    if (vagaEstaDisponivel(listaVagasDisponiveis, it)) {
                        vagaDetalheDialog.vaga = listaVagasDisponiveis.stream()
                                .filter { v ->
                                    v.vaga!! == it.vaga && v.disponibilidade.equals("disponível")
                                }.findFirst().get()
                    } else {
                        vagaDetalheDialog.vaga = it
                    }

                    if (buscarVagaDoUsuario(listaVagasSorteadas, listaVagasDisponiveis) != null) {
                        vagaDetalheDialog.possuiVaga = true
                        vagaDetalheDialog.vagaDoUsuario = buscarVagaDoUsuario(listaVagasSorteadas, listaVagasDisponiveis)!!
                    }
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

    private fun vagaEstaDisponivel(listaVagasDisponiveis: List<Vaga>, it: Vaga) : Boolean {
        return listaVagasDisponiveis.stream().filter {
            v ->
            v.vaga!! == it.vaga && v.disponibilidade.equals("disponível") }.findAny().isPresent
    }

    private fun buscarVagaDoUsuario(listaVagasSorteadas: List<Vaga>, listaVagasDisponiveis: List<Vaga>) : Vaga? {

        if (listaVagasSorteadas.stream().filter { v -> v.email == currentUser!!.email }.findAny().isPresent) {
            return listaVagasSorteadas.stream().filter { v -> v.email == currentUser!!.email }.findFirst().get()
        }

        if (listaVagasDisponiveis.stream().filter { v -> v.emailOcupante == currentUser!!.email }.findAny().isPresent) {
            return listaVagasDisponiveis.stream().filter { v -> v.emailOcupante == currentUser!!.email }.findFirst().get()
        }

        else {
            return null
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            vagasDrawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
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

}
