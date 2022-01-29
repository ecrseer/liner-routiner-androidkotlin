package br.infnet.dk_tp1.ui.tarefa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.infnet.dk_tp1.R

class TarefaFragment : Fragment() {

    companion object {
        fun newInstance() = TarefaFragment()
    }

        private lateinit var viewModel: TarefaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tarefa_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TarefaViewModel::class.java)
        // TODO: Use the ViewModel
    }

}