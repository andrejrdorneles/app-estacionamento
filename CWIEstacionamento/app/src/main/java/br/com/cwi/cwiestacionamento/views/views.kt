package br.com.cwi.cwiestacionamento.views

interface LoginView {
    fun onLoginSucceeded()
    fun onLoginFailed()
    fun onNormalLoginFailed(reason: String?)
}