package br.com.cwi.cwiestacionamento.utils

import android.app.Activity
import android.net.Uri
import br.com.cwi.cwiestacionamento.services.SharedPreferencesService
import br.com.cwi.cwiestacionamento.services.VagasService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo

object UserHolder {
    var user: FirebaseUser? = null
        set(value) {
            field = value
            field?.uid?.let {
                VagasService.initialize()
            }
        }

    var signInOptions: GoogleSignInOptions? = null

    fun isLoggedIn() = user != null

    fun logOut(activity: Activity) {
        FirebaseAuth.getInstance().signOut()

        signInOptions?.let {
            val client = GoogleSignIn.getClient(activity, it)
            client.signOut()
        }
        this.user = null
        SharedPreferencesService.remove(PessoaDados.EMAIL.value)
        SharedPreferencesService.remove(PessoaDados.NAME.value)
        SharedPreferencesService.remove(PessoaDados.IMAGE.value)
    }

    fun saveActualUser(currentUser : FirebaseUser) {
        this.user = currentUser

        var name : String? = null
        var email: String? = null
        var image : Uri? = null

        if (currentUser?.providerId != null) {
            for (userInfo: UserInfo in currentUser!!.providerData) {
                name = userInfo.displayName
                email = userInfo.email
                image = userInfo.photoUrl
            }
        } else {
            name = currentUser.displayName
            email = currentUser.email
            image = currentUser.photoUrl
        }

        SharedPreferencesService.write(PessoaDados.EMAIL.value, email)
        SharedPreferencesService.write(PessoaDados.NAME.value, name)

        if (image != null) SharedPreferencesService.write(PessoaDados.IMAGE.value, image.toString())
    }
}