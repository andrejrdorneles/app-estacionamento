package br.com.cwi.cwiestacionamento.activities

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.cwi.cwiestacionamento.R
import br.com.cwi.cwiestacionamento.models.Pessoa
import br.com.cwi.cwiestacionamento.presenters.PerfilPresenter
import br.com.cwi.cwiestacionamento.utils.loadImage
import br.com.cwi.cwiestacionamento.views.PerfilView
import com.google.firebase.auth.UserInfo
import kotlinx.android.synthetic.main.activity_perfil.*
import kotlinx.android.synthetic.main.toolbar.*

class PerfilActivity : AppCompatActivity(), PerfilView {

    private val presenter: PerfilPresenter by lazy {
        PerfilPresenter(this)
    }

    private var currentUser = presenter.getCurrentUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        if (currentUser?.providerId != null) {
            for (userInfo: UserInfo in currentUser!!.providerData) {
                personName.setText(userInfo.displayName)
                personEmail.setText(userInfo.email)
                setImageProfile(userInfo.photoUrl)
            }
        } else {
            personName.setText(currentUser?.displayName)
            personEmail.setText(currentUser?.email)
            setImageProfile(currentUser?.photoUrl)
        }

        savePerfilButton.setOnClickListener {
            save()
        }
    }

    private fun setImageProfile(uri : Uri?) {
        if (uri != null) {
            circleImageViewPerfil.loadImage(uri.toString())
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun save() {
        val pessoa = Pessoa()
        pessoa.displayName = personName.text.toString()
        pessoa.email = personEmail.text.toString()
        pessoa.actualPassword = personActualPassword.text.toString()
        pessoa.newPassword = personNewPassword.text.toString()
        presenter.save(pessoa)
    }

    override fun onSaveSuccess() {
        Toast.makeText(this, getString(R.string.save_perfil_success), Toast.LENGTH_SHORT).show()
    }

    override fun onSaveFailed() {
        Toast.makeText(this, getString(R.string.save_perfil_failed_), Toast.LENGTH_SHORT).show()
    }
}
