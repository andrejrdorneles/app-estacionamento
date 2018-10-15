package br.com.cwi.cwiestacionamento.services

import br.com.cwi.cwiestacionamento.models.Vaga
import br.com.cwi.cwiestacionamento.models.VagaDisponibilizada
import com.google.firebase.database.*

object VagasService : ChildEventListener {

    private val database: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    private lateinit var reference: DatabaseReference

    fun initialize() {
        reference = database.getReference("0").child("disponiveis")
        reference.addChildEventListener(this)
    }

    fun disponibilizar(vaga: Vaga) {
        val reference = reference.push()
//        reference.key?.let { vaga.vaga = it.toLong() }
        reference.setValue(VagaDisponibilizada(vaga))
    }

    override fun onCancelled(p0: DatabaseError) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChildChanged(p0: DataSnapshot, p1: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
        return
    }

    override fun onChildRemoved(p0: DataSnapshot) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



}
