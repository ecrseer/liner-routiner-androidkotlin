package br.infnet.dk_tp1.ui.tarefa

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.infnet.dk_tp1.databinding.MicrotarefaItemFragmentBinding
import br.infnet.dk_tp1.domain.MicroTarefa

class MicroTarefasRecyclerViewAdapter(
    private var listaMicrotarefas: List<String>,
    val funcaoParaClic:(Int)->Unit
) : RecyclerView.Adapter<MicroTarefasRecyclerViewAdapter.ViewHolder>() {

    fun mudarLista(novaListaMicrotarefas: List<String>){
        listaMicrotarefas = novaListaMicrotarefas
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val minhaBindingView = MicrotarefaItemFragmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)

        val microtarefaViewHolder = ViewHolder(minhaBindingView,funcaoParaClic)

        return microtarefaViewHolder

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaMicrotarefas[position]
        holder.descricao.setText( item)

    }

    override fun getItemCount(): Int = listaMicrotarefas.size

    inner class ViewHolder(binding: MicrotarefaItemFragmentBinding,funcaoDeClic:(Int)->Unit ) : RecyclerView.ViewHolder(binding.root) {
        val descricao: EditText = binding.microtarefaDescricao
        /*val fundo: FrameLayout = binding.itemImgContainer

        init{
            fundo.setOnClickListener {
                funcaoDeClic(bindingAdapterPosition)
            }*/
        }

        override fun toString(): String {
            return super.toString() + " '"
        }

    }


