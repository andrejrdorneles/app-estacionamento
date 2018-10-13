package br.com.cwi.cwiestacionamento.presenters

import br.com.cwi.cwiestacionamento.models.Pessoa
import br.com.cwi.cwiestacionamento.utils.UserHolder
import br.com.cwi.cwiestacionamento.views.PerfilView
import com.google.firebase.auth.*

class PerfilPresenter(private val view: PerfilView) {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val user = firebaseAuth.currentUser

    fun save(pessoa: Pessoa) {
        val shouldUpdatePassword = !pessoa.newPassword.isNullOrBlank()
        val hasErrorUpdatePassword = shouldUpdatePassword && pessoa.actualPassword.isNullOrBlank()
        if (pessoa.email.isNullOrBlank() || pessoa.displayName.isNullOrBlank() || hasErrorUpdatePassword) {
            view.onSaveFailed()
            return
        }

        if (shouldUpdatePassword) {
            updatePassword(pessoa)
        }

        if (!pessoa.email.equals(user?.email)) {
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
                        view.onSaveFailed(it.exception!!.localizedMessage)
                    }
                }
    }

    private fun updatePassword(person: Pessoa) {
        var credential = EmailAuthProvider.getCredential(person.email!!, person.actualPassword!!)
        user?.reauthenticate(credential)
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        user.updatePassword(person.newPassword!!)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        updateCurrentUser()
                                    } else {
                                        view.onSaveFailed()
                                    }
                                }
                    } else {
                        view.onSaveFailed(it.exception!!.localizedMessage)
                    }
                }
    }

    private fun updateEmail(pessoa: Pessoa) {
        user?.updateEmail(pessoa.email!!)?.addOnCompleteListener {
            if (it.isSuccessful) {
                updateCurrentUser()
            } else {
                view.onSaveFailed(it.exception!!.localizedMessage)
            }
        }
    }

    private fun updateCurrentUser() {
        UserHolder.user = firebaseAuth.currentUser
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}