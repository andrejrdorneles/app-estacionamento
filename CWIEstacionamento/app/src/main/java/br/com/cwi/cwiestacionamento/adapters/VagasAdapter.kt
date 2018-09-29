package br.com.cwi.cwiestacionamento.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.cwi.cwiestacionamento.R
import br.com.cwi.cwiestacionamento.views.Vaga
import kotlinx.android.synthetic.main.view_vagas.view.*

class VagasAdapter (private val items: ArrayList<Vaga>,
                    private val onClick: (vaga: Vaga) -> Unit)
    : RecyclerView.Adapter<VagasAdapter.ViewHolder>() {

    private var lastItemPosition: Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        LayoutInflater.from(parent.context).run {
            return ViewHolder(itemView = inflate(
                    R.layout.view_vagas,
                    parent,
                    false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items[position].run {
            holder.vagaTextView.text = vaga
            holder.nomeTextView.text = nome
            holder.disponibilidadeTextView.text = disponibilidade

            holder.itemView.setOnClickListener {
                onClick(this)
            }
        }
    }

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val vagaTextView: TextView = itemView.vagaTextView
        val nomeTextView: TextView = itemView.nomeTextView
        val disponibilidadeTextView: TextView = itemView.disponibilidadeTextView
    }
}