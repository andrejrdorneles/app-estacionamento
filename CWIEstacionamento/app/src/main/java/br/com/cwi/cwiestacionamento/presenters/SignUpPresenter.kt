package br.com.cwi.cwiestacionamento.presenters

import br.com.cwi.cwiestacionamento.utils.UserHolder
import br.com.cwi.cwiestacionamento.views.LoginView
import br.com.cwi.cwiestacionamento.views.SignUpView
import com.google.firebase.auth.FirebaseAuth

class SignUpPresenter(private val viewSignUp: SignUpView) {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun signUp(email: String, password: String) {
        if (!email.isBlank() && !password.isBlank()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            UserHolder.user = firebaseAuth.currentUser
                            viewSignUp.onSignUpSucceeded()
                        } else {
                            viewSignUp.onSignUpFailed(it.exception?.localizedMessage)
                        }
                    }
        } else {
            viewSignUp.onSignUpFailed("Dados inválidos, verifique-os e então tente novamente.")
        }
    }
}