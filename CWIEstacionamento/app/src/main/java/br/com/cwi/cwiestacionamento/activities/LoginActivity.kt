package br.com.cwi.cwiestacionamento.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import br.com.cwi.cwiestacionamento.R
import br.com.cwi.cwiestacionamento.presenters.LoginPresenter
import br.com.cwi.cwiestacionamento.utils.UserHolder
import br.com.cwi.cwiestacionamento.views.LoginView
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginView {

    private val presenter: LoginPresenter by lazy {
        LoginPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        signInGoogleButton.setOnClickListener {
            presenter.logInWithGoogle(this)
        }

        signInEmailPasswordButton.setOnClickListener {
            logInWithEmailAndPassword()
        }

        if (UserHolder.isLoggedIn()) {
            goToMainActivity()
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onLoginSucceeded() {
        goToMainActivity()
    }

    override fun onLoginFailed() {
        Toast.makeText(this, getString(R.string.error_login), Toast.LENGTH_SHORT).show()
    }

    override fun onNormalLoginFailed(reason: String?) {
        Toast.makeText(this, reason, Toast.LENGTH_LONG).show()
    }

    private fun logInWithEmailAndPassword() {
        val email = emailTextInput.text.toString()
        val password = passwordTextInput.text.toString()
        presenter.logInWithEmailAndPassword(email, password)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LoginPresenter.REQUEST_CODE) {
            presenter.handleLoginResult(data)
        }
    }
}
