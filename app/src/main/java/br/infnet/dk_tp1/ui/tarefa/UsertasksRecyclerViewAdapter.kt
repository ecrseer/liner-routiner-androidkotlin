package br.infnet.dk_tp1.ui.tarefa

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import br.infnet.dk_tp1.databinding.UsertasksItemFragmentBinding

class UsertasksRecyclerViewAdapter(
    private var usertasksList: List<String>,
    val funcaoParaClic:(Int,String)->Unit
) : RecyclerView.Adapter<UsertasksRecyclerViewAdapter.ViewHolder>() {

    fun mudarLista(newUsertasksList: List<String>) {
        usertasksList = newUsertasksList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val minhaBindingView = UsertasksItemFragmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        val usertasksViewHolder = ViewHolder(minhaBindingView, funcaoParaClic)

        return usertasksViewHolder

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = usertasksList[position]
        holder.descricao.setText(item)

    }

    override fun getItemCount(): Int = usertasksList.size

    inner class ViewHolder(
        binding: UsertasksItemFragmentBinding,
        funcaoDeClic: (Int, String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        val descricao: EditText = binding.microtarefaDescricao
        init {
            descricao.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val text = s.toString()

                    println("changed")
                    funcaoDeClic(bindingAdapterPosition, text)
                }

            })


        }

        override fun toString(): String {
            return super.toString() + " '"
        }

    }
}


