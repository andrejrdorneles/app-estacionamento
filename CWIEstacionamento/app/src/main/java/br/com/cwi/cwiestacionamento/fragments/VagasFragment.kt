package br.com.cwi.cwiestacionamento.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.com.cwi.cwiestacionamento.R
import br.com.cwi.cwiestacionamento.views.Vaga
import br.com.cwi.cwiestacionamento.service.VagasService
import br.com.cwi.cwiestacionamento.views.VagasView

class VagasFragment : Fragment(), VagasView {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_vagas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        VagasService.initialize()
    }

    override fun onResponse(list: ArrayList<Vaga>, isFirstFetch: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFailure(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDetailResponse() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDetailFailure(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
