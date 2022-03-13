package br.infnet.dk_tp1.ui.tarefa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.infnet.dk_tp1.LinerRoutinerApplication
import br.infnet.dk_tp1.databinding.UsertasksFragmentBinding
import br.infnet.dk_tp1.ui.main.MainViewModel

class UserTasksFragment : Fragment() {

    companion object {
        fun newInstance() = UserTasksFragment()

    }
    var posicao = 0L

    private var _binding: UsertasksFragmentBinding? = null
    private val binding get()= _binding!!

    private val mainViewModel by viewModels<MainViewModel>({requireParentFragment()})

    private lateinit var  userTasksViewModel: UserTasksViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = UsertasksFragmentBinding.inflate(inflater,container,false)
        setupViewModel()

        val viw = binding.root
        return viw
    }

    private fun setupViewModel(){
        arguments?.let {
            posicao = it.getInt("posicaoTarefa").toLong()

            val linerApp = requireActivity().application as LinerRoutinerApplication

            mainViewModel.horarios2.value?.let{horarios->
                val ufactory = UserTasksViewModelFactory(horarios,posicao)
                userTasksViewModel = ViewModelProvider(this,ufactory)
                    .get(UserTasksViewModel::class.java)
            }


        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userTasksViewModel.userTasks.observe(viewLifecycleOwner, Observer {
            it?.let{
                with (binding.recyclerTodos){
                    adapter = UsertasksRecyclerViewAdapter(it) { posicao, newText ->
                        userTasksViewModel.modifyUserTaskByPosition(posicao,newText)
                    }
                }
            }
        })

        binding.fabSalvarTarefa.setOnClickListener {
            val userTasks = userTasksViewModel.userTasks.value
            val posicaoHorario:Long = posicao
            mainViewModel.editarHorario(userTasks,posicaoHorario)
        }
        binding.fabAdicionaTodo.setOnClickListener{
            userTasksViewModel.addUserTask()
        }

    }


}