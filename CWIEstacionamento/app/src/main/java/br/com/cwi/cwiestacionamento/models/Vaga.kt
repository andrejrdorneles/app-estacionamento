package br.com.cwi.cwiestacionamento.models

class Vaga {

    var vaga: Long? = null
    var name: String? = null
    var email: String? = null
    var disponibilidade: String? = null

    constructor() {}

    constructor(vaga: Long?, name: String?, email: String?, disponibilidade: String?) {
        this.vaga = vaga
        this.name = name
        this.email = email
        this.disponibilidade = disponibilidade
    }
}