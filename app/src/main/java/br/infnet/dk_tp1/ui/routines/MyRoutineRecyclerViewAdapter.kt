package br.infnet.dk_tp1.ui.routines

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.infnet.dk_tp1.databinding.FragmentRoutineBinding
import br.infnet.dk_tp1.domain.Routine


/**
 * [RecyclerView.Adapter] that can display a [Routine].
 * TODO: Replace the implementation with code for your data type.
 */
class MyRoutineRecyclerViewAdapter(
    private val values: List<Routine>,

    val funcaoParaClic:(Int)->Unit
) : RecyclerView.Adapter<MyRoutineRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val bind = FragmentRoutineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val vw = ViewHolder(bind,funcaoParaClic)
        return vw

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.idRoutine.toString()
        holder.contentView.text = item.name
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentRoutineBinding, funcaoDeClic: (Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        val fundo: LinearLayout = binding.routineitemContainer

        init {
            fundo.setOnClickListener {
                funcaoDeClic(bindingAdapterPosition)
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}