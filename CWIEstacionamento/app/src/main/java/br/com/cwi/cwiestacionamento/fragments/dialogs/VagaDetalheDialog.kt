package br.com.cwi.cwiestacionamento.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.cwi.cwiestacionamento.R
import br.com.cwi.cwiestacionamento.models.Vaga
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.dialog_vaga_detalhe.view.*
import kotlinx.android.synthetic.main.dialog_vaga_reservada.view.*


class VagaDetalheDialog : DialogFragment() {

    lateinit var vaga: Vaga
    lateinit var vagaDoUsuario: Vaga
    var possuiVaga: Boolean = false

    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var disponibilidadeVagasRef: DatabaseReference = database.getReference("0").child("disponiveis")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (possuiVaga) {
            var view = inflater.inflate(R.layout.dialog_vaga_reservada, container, false)

            view.buttonCancelar.setOnClickListener {
                this.dismiss()
            }

            view.buttonDisponibilizarVagaReservada.setOnClickListener {
                disponibilidadeVagasRef.child(vagaDoUsuario.id!!).updateChildren(updateDBDisponibilizarVaga())
                this.dismiss()
            }

            return view

        } else if (vaga.disponibilidade.equals("disponível")) {
            var view = inflater.inflate(R.layout.dialog_vaga_detalhe, container, false)

            view.buttonCancelarVaga.setOnClickListener {
                this.dismiss()
            }

            view.buttonSelecionarVaga.setOnClickListener {
                disponibilidadeVagasRef.child(vaga.id!!).updateChildren(updateDBSelecionarVaga())
                this.dismiss()
            }
            return view
        } else {
            return inflater.inflate(R.layout.dialog_vaga_indisponivel, container, false)
        }
    }

    private fun updateDBDisponibilizarVaga(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        setDisponibilidadeDisponivel(map)
        setEmailOcupanteVazio(map)
        return map
    }

    private fun setDisponibilidadeDisponivel(map: HashMap<String, Any>) {
        map["disponibilidade"] = "disponível"
    }

    private fun setEmailOcupanteVazio(map: HashMap<String, Any>) {
        map["emailOcupante"] = ""
    }

    private fun updateDBSelecionarVaga(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        setDisponibilidadeOcupada(map)
        setEmailOcupante(map)
        return map
    }

    private fun setDisponibilidadeOcupada(map: HashMap<String, Any>) {
        map["disponibilidade"] = "ocupada"
    }

    private fun setEmailOcupante(map: HashMap<String, Any>) {
        map["emailOcupante"] = FirebaseAuth.getInstance().currentUser?.email.toString()
    }
}
