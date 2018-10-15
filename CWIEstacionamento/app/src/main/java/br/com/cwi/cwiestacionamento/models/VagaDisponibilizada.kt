package br.com.cwi.cwiestacionamento.models

import android.icu.text.SimpleDateFormat
import java.util.*

class VagaDisponibilizada(vaga : Vaga) : Vaga(vaga.vaga, vaga.name, vaga.email, "disponível", SimpleDateFormat("dd/M/yyyy").format(Date()).toString()) {

    var emailOcupante : String? = null

}