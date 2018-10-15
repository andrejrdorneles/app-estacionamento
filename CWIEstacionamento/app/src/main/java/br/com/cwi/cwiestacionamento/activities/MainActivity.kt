package br.com.cwi.cwiestacionamento.activities

import android.content.Intent
import android.icu.text.SimpleDateFormat
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

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var vagasRef: DatabaseReference = database.getReference("0").child("vagas")
    var vagasDispRef: Query = database.getReference("0")
            .child("disponiveis").orderByChild("data")
            .equalTo(SimpleDateFormat("dd/M/yyyy").format(Date()).toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vagasDispRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val list = ArrayList<Vaga>()
                for (vagaSnapshot in p0.children) {
                    val vaga = vagaSnapshot.getValue(Vaga::class.java)!!
                    if (vaga.disponibilidade.equals("disponível")) { //TODO ENUM STATUS VAGA
                        list.add(vaga)
                    }
                }
                numeroVagasDisponiveis.text = list.size.toString()
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

        vagaUsuario.setText(getVagaUsuarioText())

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun getVagaDoUsuario(): Vaga{
        var vagaUsuario = Vaga()
        vagasRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                for (vagaSnapshot in p0.children) {
                    val vaga = vagaSnapshot.getValue(Vaga::class.java)!!
                    if (vaga.email.equals(SharedPreferencesService.retrieveString(PessoaDados.EMAIL.value))) { //TODO ENUM STATUS VAGA
                        vagaUsuario = vaga
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

        return vagaUsuario
    }
    private fun getVagaUsuarioText(): String {

        var vaga = getVagaDoUsuario()

        if(vaga.email != null)
            return getString(R.string.temVaga)
        else
            return getString(R.string.naoTemVaga)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuHeaderPersonEmail.text = SharedPreferencesService.retrieveString(PessoaDados.EMAIL.value)
        menuHeaderPersonName.text = SharedPreferencesService.retrieveString(PessoaDados.NAME.value)
        val uriProfileImage = SharedPreferencesService.retrieveString(PessoaDados.IMAGE.value)

        if (!uriProfileImage.isNullOrBlank()) {
            personImageHeaderMenu.loadImage(uriProfileImage)
        }
        menuInflater.inflate(R.menu.main, menu)
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
