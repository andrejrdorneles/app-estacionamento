package br.com.cwi.cwiestacionamento.presenters

import br.com.cwi.cwiestacionamento.models.Pessoa
import br.com.cwi.cwiestacionamento.utils.UserHolder
import br.com.cwi.cwiestacionamento.views.PerfilView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class PerfilPresenter(private val view : PerfilView) {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val user = firebaseAuth.currentUser

    fun save(pessoa: Pessoa) {
        if (pessoa.email != null && !pessoa.email.equals(user?.email)) {
            updateEmail(pessoa)
        }
        
        val changeRequest = UserProfileChangeRequest.Builder()
                .setDisplayName(pessoa.displayName)
                .setPhotoUri(pessoa.photoURL)
                .build()

        user?.updateProfile(changeRequest)
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        updateCurrentUser()
                        view.onSaveSuccess()
                    } else {
                        view.onSaveFailed()
                    }
                }
    }

    private fun updateEmail(pessoa: Pessoa) {
        user?.updateEmail(pessoa.email!!)?.addOnCompleteListener {
            if (it.isSuccessful) {
                updateCurrentUser()
            }
        }
    }

    private fun updateCurrentUser() {
        UserHolder.user = firebaseAuth.currentUser
    }

    fun getCurrentUser() : FirebaseUser? {
        return firebaseAuth.currentUser
    }
}