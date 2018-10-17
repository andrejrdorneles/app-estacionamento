package br.com.cwi.cwiestacionamento.services

import br.com.cwi.cwiestacionamento.models.Vaga
import com.google.firebase.auth.FirebaseAuth

class VagasService {

    private var currentUser = FirebaseAuth.getInstance().currentUser

    fun vagaEstaDisponivel(listaVagasDisponiveis: List<Vaga>, it: Vaga): Boolean {
        return listaVagasDisponiveis.stream().filter { v ->
            v.vaga!! == it.vaga && v.disponibilidade.equals("disponível")
        }.findAny().isPresent
    }

    fun buscarVagaDoUsuario(listaVagasSorteadas: List<Vaga>, listaVagasDisponiveis: List<Vaga>): Vaga? {

        if (listaVagasSorteadas.stream().filter { v -> v.email == currentUser!!.email }.findAny().isPresent) {
            return listaVagasSorteadas.stream().filter { v -> v.email == currentUser!!.email }.findFirst().get()
        }

        if (listaVagasDisponiveis.stream().filter { v -> v.emailOcupante == currentUser!!.email }.findAny().isPresent) {
            return listaVagasDisponiveis.stream().filter { v -> v.emailOcupante == currentUser!!.email }.findFirst().get()
        } else {
            return null
        }
    }
}