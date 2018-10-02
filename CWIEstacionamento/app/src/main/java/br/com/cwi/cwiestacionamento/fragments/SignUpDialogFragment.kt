package br.com.cwi.cwiestacionamento.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.cwi.cwiestacionamento.R
import br.com.cwi.cwiestacionamento.activities.MainActivity
import br.com.cwi.cwiestacionamento.presenters.SignUpPresenter
import br.com.cwi.cwiestacionamento.utils.UserHolder
import br.com.cwi.cwiestacionamento.views.LoginView
import br.com.cwi.cwiestacionamento.views.SignUpView
import kotlinx.android.synthetic.main.fragment_sign_up_dialog.*
import kotlinx.android.synthetic.main.fragment_sign_up_dialog.view.*

class SignUpDialogFragment : DialogFragment(), SignUpView {

    private val presenterSignUp: SignUpPresenter by lazy {
        SignUpPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up_dialog, container, false)

        view.signUpButton.setOnClickListener {
            signUp()
        }

        return view
    }

    override fun onSignUpSucceeded() {
        Toast.makeText(context, "Olá, ${UserHolder.user?.email}", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onSignUpFailed(reason: String?) {
        Toast.makeText(context, reason, Toast.LENGTH_LONG).show()
    }


    private fun signUp() {
        val email = signUpEmail.text.toString()
        val password = signUpPassword.text.toString()
        presenterSignUp.signUp(email, password)
    }
}