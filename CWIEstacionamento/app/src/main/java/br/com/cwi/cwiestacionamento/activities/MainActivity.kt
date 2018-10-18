package br.com.cwi.cwiestacionamento.activities

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import br.com.cwi.cwiestacionamento.R
import kotlinx.android.synthetic.main.activity_main.*
import br.com.cwi.cwiestacionamento.services.SharedPreferencesService
import br.com.cwi.cwiestacionamento.models.Vaga
import br.com.cwi.cwiestacionamento.services.VagasService
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

    var vagasDispRef: Query = database.getReference("0")
            .child("disponiveis").orderByChild("data")
            .equalTo(SimpleDateFormat("dd/M/yyyy").format(Date()).toString())

    val listaVagasDisponiveis = ArrayList<Vaga>()

    val vagasDisponibilizadasOcupadas = ArrayList<Vaga>()

    var vagasRef: DatabaseReference = database.getReference("0").child("vagas")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vagasDispRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                listaVagasDisponiveis.clear()
                for (vagaSnapshot in p0.children) {
                    val vaga = vagaSnapshot.getValue(Vaga::class.java)!!
                    if (vaga.disponibilidade.equals("disponível")) { //TODO ENUM STATUS VAGA
                        listaVagasDisponiveis.add(vaga)
                    }else{
                        vagasDisponibilizadasOcupadas.add(vaga)
                    }
                }
                
                numeroVagasDisponiveis.text = listaVagasDisponiveis.size.toString()
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

        vagasRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                var possuiVagaFixa = false
                for (vagaSnapshot in p0.children) {
                    val vaga = vagaSnapshot.getValue(Vaga::class.java)!!
                    possuiVagaFixa = vaga.email.equals(SharedPreferencesService.retrieveString(PessoaDados.EMAIL.value))
                    if(possuiVagaFixa){
                        mostrarVagaAtual(vaga.vaga.toString())
                        val vagaEstaDisponivel = listaVagasDisponiveis.find { it.vaga == vaga.vaga} != null
                        val vagaOcupada = vagasDisponibilizadasOcupadas.find { it.vaga == vaga.vaga}
                        if(vagaEstaDisponivel){
                            vagaUsuarioText.text = getString(R.string.tem_vaga)
                        }else if(vagaOcupada != null){
                            vagaUsuarioText.text = getString(R.string.tem_vaga_ocupada) + vagaOcupada.emailOcupante
                        }else{
                            vagaUsuarioText.text = getString(R.string.tem_vaga_disponivel)
                            disponibilizarVagaButton.isEnabled = true
                            disponibilizarVagaButton.setOnClickListener {
                                VagasService.disponibilizar(vaga)
                                disponibilizarVagaButton.isEnabled = false
                                vagaUsuarioText.text = getString(R.string.tem_vaga)
                            }
                        }
                        break
                    }

                }

                if(!possuiVagaFixa){
                    val vagaUsuario = vagasDisponibilizadasOcupadas.find { it.emailOcupante.equals(SharedPreferencesService.retrieveString(PessoaDados.EMAIL.value))}
                    if (vagaUsuario != null){
                        mostrarVagaAtual(vagaUsuario.vaga.toString())
                        vagaUsuarioText.text = getString(R.string.usando_vaga_de) + vagaUsuario.email
                    }else {
                        vagaUsuarioText.text = getString(R.string.nao_tem_vaga)
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        navigationView.setNavigationItemSelectedListener(this)

        verVagasButton.setOnClickListener {
            intent = Intent(this, VagasActivity::class.java)
            startActivity(intent)
        }

    }

    fun mostrarVagaAtual(vaga : String){
        vagaAtualInfo.visibility = View.VISIBLE
        numeroVagaAtual.text = vaga
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuHeaderPersonEmail.text = SharedPreferencesService.retrieveString(PessoaDados.EMAIL.value)
        menuHeaderPersonName.text = SharedPreferencesService.retrieveString(PessoaDados.NAME.value)
        val uriProfileImage = SharedPreferencesService.retrieveString(PessoaDados.IMAGE.value)

        if (!uriProfileImage.isNullOrBlank()) {
            personImageHeaderMenu.loadImage(uriProfileImage)
        }
        
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            mainDrawerLayout.openDrawer(GravityCompat.START)
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.nav_home -> {
                mainDrawerLayout.closeDrawers()
                return true
            }
            R.id.nav_vagas_disponiveis -> {
                intent = Intent(this, VagasDisponiveisActivity::class.java)
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
