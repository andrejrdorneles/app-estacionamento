package br.com.cwi.cwiestacionamento.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import br.com.cwi.cwiestacionamento.R
import br.com.cwi.cwiestacionamento.models.Vaga
import kotlinx.android.synthetic.main.view_vagas.view.*

class VagasAdapter (private val items: ArrayList<Vaga>,
                    private val disponiveis: ArrayList<Vaga>,
                    private val onClick: (clickVaga: Vaga) -> Unit)
    : RecyclerView.Adapter<VagasAdapter.VagaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VagaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_vagas, parent, false)

        return VagaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VagaViewHolder, position: Int) {

        for (item in items) {
            if (disponiveis.stream().filter { v -> v.vaga!!.equals(item.vaga) }.findFirst().isPresent) {
                item.disponibilidade = disponiveis.stream().filter { v -> v.vaga!!.equals(item.vaga) }.findFirst().get().disponibilidade
            }
        }

        items[position].run {
            holder.nomeTextView.text = name
            holder.vagaTextView.text = vaga.toString()
            holder.disponibilidadeTextView.text = disponibilidade
            holder.itemView.setOnClickListener{
                onClick(this)
            }
            holder.vagaCheckbox.isChecked = disponibilidade == "disponível"
        }
    }


    override fun getItemCount() = items.size

    class VagaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vagaTextView: TextView = itemView.vagaTextView
        val nomeTextView: TextView = itemView.nomeTextView
        val disponibilidadeTextView: TextView = itemView.disponibilidadeTextView
        val vagaCheckbox: CheckBox = itemView.vagaCheckbox
    }
}