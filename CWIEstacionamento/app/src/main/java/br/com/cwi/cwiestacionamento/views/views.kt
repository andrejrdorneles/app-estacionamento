package br.com.cwi.cwiestacionamento.views

interface LoginView {
    fun onLoginSucceeded()
    fun onLoginFailed()
    fun onNormalLoginFailed(reason: String?)
}

open class Vaga(
        val vaga: String,
        val nome: String,
        val disponibilidade: String
)

interface VagasView {
    fun onResponse(list: ArrayList<Vaga>, isFirstFetch: Boolean)
    fun onFailure(throwable: Throwable)
    fun onDetailResponse()
    fun onDetailFailure(throwable: Throwable)
}