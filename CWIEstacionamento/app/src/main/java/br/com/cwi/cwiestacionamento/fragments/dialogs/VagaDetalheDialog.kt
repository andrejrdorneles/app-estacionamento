package br.com.cwi.cwiestacionamento.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.cwi.cwiestacionamento.R
import br.com.cwi.cwiestacionamento.models.Vaga

class VagaDetalheDialog : DialogFragment() {

    lateinit var vaga: Vaga

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (vaga.disponibilidade.equals("disponível")) {
            return inflater.inflate(R.layout.dialog_vaga_detalhe, container, false)
        } else {
            return inflater.inflate(R.layout.dialog_vaga_indisponivel, container, false)
        }
    }
}