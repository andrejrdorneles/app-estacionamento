package br.com.cwi.cwiestacionamento

import android.app.Application
import android.content.Context
import br.com.cwi.cwiestacionamento.services.SharedPreferencesService
import br.com.cwi.cwiestacionamento.utils.UserHolder
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class CWIEstacionamentoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val sharedPreferences =
                this.getSharedPreferences("CWIEstacionamentoApplication", Context.MODE_PRIVATE)

        SharedPreferencesService.sharedPreferences = sharedPreferences

        FirebaseApp.initializeApp(this)

        UserHolder.user = FirebaseAuth.getInstance().currentUser
    }
}