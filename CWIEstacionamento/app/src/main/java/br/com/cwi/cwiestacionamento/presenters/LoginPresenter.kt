package br.com.cwi.cwiestacionamento.presenters

import android.app.Activity
import android.content.Intent
import br.com.cwi.cwiestacionamento.R
import br.com.cwi.cwiestacionamento.services.SharedPreferencesService
import br.com.cwi.cwiestacionamento.utils.UserHolder
import br.com.cwi.cwiestacionamento.views.LoginView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginPresenter(private val view: LoginView) {

    companion object {
        const val REQUEST_CODE = 9000
    }

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun logInWithGoogle(activity: Activity) {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .build()

        UserHolder.signInOptions = options
        val client = GoogleSignIn.getClient(activity, options)
        activity.startActivityForResult(client.signInIntent, REQUEST_CODE)
    }

    fun handleLoginResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)

        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener {
                        if (task.isSuccessful) {
                            UserHolder.user = firebaseAuth.currentUser
                            onLoginSucceeded()
                        } else {
                            view.onLoginFailed()
                        }
                    }
        } catch (exception: ApiException) {
            view.onLoginFailed()
        }
    }

    fun logInWithEmailAndPassword(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            view.onLoginFailed()
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        UserHolder.user = firebaseAuth.currentUser
                        onLoginSucceeded()
                    } else {
                        view.onNormalLoginFailed(it.exception?.localizedMessage)
                    }
                }
    }

    private fun onLoginSucceeded() {
        val currentUser = firebaseAuth.currentUser
        UserHolder.user = currentUser
        SharedPreferencesService.write("email", currentUser?.email.toString())
        SharedPreferencesService.write("nome", currentUser?.displayName.toString())
        view.onLoginSucceeded()
    }
}