package br.com.cwi.cwiestacionamento.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.MenuItem
import br.com.cwi.cwiestacionamento.adapters.VagasAdapter
import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot
import br.com.cwi.cwiestacionamento.models.Vaga
import br.com.cwi.cwiestacionamento.R
import br.com.cwi.cwiestacionamento.dialogs.VagaDetalheDialog
import br.com.cwi.cwiestacionamento.services.VagasUtilsService
import kotlinx.android.synthetic.main.activity_vagas.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class VagasActivity : BaseActivity() {

    lateinit var myRecyclerView: RecyclerView
    lateinit var vagasAdapter: VagasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

                listaVagasDisponiveis.clear()

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

                    if (VagasUtilsService().vagaEstaDisponivel(listaVagasDisponiveis, it)) {
                        vagaDetalheDialog.vaga = listaVagasDisponiveis.stream()
                                .filter { v ->
                                    v.vaga!! == it.vaga && v.disponibilidade.equals("disponível")
                                }.findFirst().get()
                    } else {
                        vagaDetalheDialog.vaga = it
                    }

                    if (VagasUtilsService().buscarVagaDoUsuario(listaVagasSorteadas, listaVagasDisponiveis) != null) {
                        vagaDetalheDialog.possuiVaga = true
                        vagaDetalheDialog.vagaDoUsuario = VagasUtilsService().buscarVagaDoUsuario(listaVagasSorteadas, listaVagasDisponiveis)!!
                    }
                    vagaDetalheDialog.show(supportFragmentManager, "tag")
                }

                myRecyclerView = findViewById(R.id.recyclerViewListaVagas)
                myRecyclerView.layoutManager = LinearLayoutManager(this@VagasActivity)
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
                intent = Intent(this, VagasDisponiveisActivity::class.java)
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
            else -> {
                return false
            }
        }
        vagasDrawerLayout.closeDrawers()
        startActivity(intent)
        return true
    }

    override fun getContentView(): Int {
        return R.layout.activity_vagas
    }

    override fun openDrawers() {
        vagasDrawerLayout.openDrawer(Gravity.START)
    }

    override fun closeDrawers() {
        vagasDrawerLayout.closeDrawers()
    }
}

