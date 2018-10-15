package br.com.cwi.cwiestacionamento.views

interface LoginView {
    fun onLoginSucceeded()
    fun onLoginFailed()
    fun onNormalLoginFailed(reason: String?)
}

interface SignUpView {
    fun onSignUpSucceeded()
    fun onSignUpFailed(reason: String?)
}

interface PerfilView {
    fun save()
    fun onSaveSuccess()
    fun onSaveFailed()
    fun onSaveFailed(reason : String)
}