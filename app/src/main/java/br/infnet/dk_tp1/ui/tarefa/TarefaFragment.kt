package br.infnet.dk_tp1.ui.tarefa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.infnet.dk_tp1.LinerRoutinerApplication
import br.infnet.dk_tp1.R
import br.infnet.dk_tp1.databinding.MainFragmentBinding
import br.infnet.dk_tp1.databinding.TarefaFragmentBinding
import br.infnet.dk_tp1.service.HorarioAndTarefaRepository
import br.infnet.dk_tp1.service.TarefaRepository
import br.infnet.dk_tp1.ui.main.MainFragment
import br.infnet.dk_tp1.ui.main.MainViewModel

class TarefaFragment : Fragment() {

    companion object {
        fun newInstance() = TarefaFragment()

    }
    var posicao = 0L

    private var _binding: TarefaFragmentBinding? = null
    private val binding get()= _binding!!

    private lateinit var  viewModel: TarefaViewModel /*by viewModels { TarefaViewModelFactory(
        (requireActivity().application as LinerRoutinerApplication).tarefaRepository,0
    ) }*/
    inner class TarefaViewModelFactory (private val repository: TarefaRepository,
                                        private val idTarefa:Long): ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(TarefaViewModel::class.java)){
                return TarefaViewModel(repository,idTarefa) as T;
            }
            throw IllegalArgumentException(" ViewModel instanciando errado")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TarefaFragmentBinding.inflate(inflater,container,false)

        val viw = binding.root
        return viw
    }

    private fun setupViewModel(){
        arguments?.let {
            posicao = it.getInt("posicaoTarefa").toLong()
            val linerApp = requireActivity().application as LinerRoutinerApplication
            val supostoId:Long = posicao+1
            val factory = TarefaViewModelFactory(linerApp.tarefaRepository,supostoId)
            viewModel = ViewModelProvider(this,factory).get(TarefaViewModel::class.java)
            viewModel.posicaoId.observe(viewLifecycleOwner,Observer{
                it?.let{

                }
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabSalvarTarefa.setOnClickListener {
            binding.txtTitulo?.text?.toString()?.let{
                viewModel.editarTarefa(it)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        setupViewModel()
        viewModel.tarefa.observe(viewLifecycleOwner, Observer {
            binding.txtTitulo.setText(it.nome)
        })
    }

}