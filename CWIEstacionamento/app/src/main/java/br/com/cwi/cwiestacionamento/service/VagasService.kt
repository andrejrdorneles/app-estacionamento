package br.com.cwi.cwiestacionamento.service

import br.com.cwi.cwiestacionamento.views.Vaga
import com.google.firebase.database.*

object VagasService : ChildEventListener {

    private val database: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    val vagas = ArrayList<Vaga>()

    private lateinit var reference: DatabaseReference

    fun initialize() {
        vagas.clear()
        reference = database.getReference("0").child("vagas")
        reference.addChildEventListener(this)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChildRemoved(p0: DataSnapshot) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}