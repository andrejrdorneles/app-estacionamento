package br.com.cwi.cwiestacionamento.models

class Vaga {

    var id: String? = null
    var vaga: Long? = null
    var name: String? = null
    var email: String? = null
    var emailOcupante: String? = null
    var disponibilidade: String? = null
    var data: String? = null

    constructor() {}

    constructor(id: String?, vaga: Long?, name: String?, email: String?, emailOcupante: String?, disponibilidade: String?, data: String?) {
        this.id = id
        this.vaga = vaga
        this.name = name
        this.email = email
        this.emailOcupante = emailOcupante
        this.disponibilidade = disponibilidade
        this.data = data
    }
}