package br.infnet.dk_tp1.ui.tarefa

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.infnet.dk_tp1.LinerRoutinerApplication
import br.infnet.dk_tp1.databinding.UsertasksFragmentBinding
import br.infnet.dk_tp1.ui.login.afterTextChanged
import br.infnet.dk_tp1.ui.main.MainViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import java.lang.Exception


fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    var lastInput = ""
    var debounceJob: Job? = null
    val uiScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    val delayMillis=1400L
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            if (editable != null) {
                val newtInput = editable.toString()
                debounceJob?.cancel()
                if (lastInput != newtInput) {
                    lastInput = newtInput
                    debounceJob = uiScope.launch {
                        delay(delayMillis)
                        if (lastInput == newtInput) {
                            afterTextChanged.invoke(editable.toString())
                        }
                    }
                }

            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}


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
        userTasksViewModel.userTaskBackgroundUrl.observe(viewLifecycleOwner, Observer {
            if(it.contains("http")){
                Picasso.get().load(it)
                    //.centerInside()
                    .centerCrop().resize(580, 700)
                    .into(binding.imageView3TempHolder,object : Callback{
                        override fun onSuccess() {
                            binding.recyclerTodos.setBackgroundDrawable(binding.imageView3TempHolder.getDrawable());
                        }

                        override fun onError(e: Exception?) {}

                    })
            }

        })

        binding.fabSalvarTarefa.setOnClickListener {
            val userTasks = userTasksViewModel.userTasks.value
            val posicaoHorario:Long = posicao
            val title = binding.txtTitulo.text.toString()
            userTasksViewModel.getContextImage(title)
            mainViewModel.editarHorario(userTasks,posicaoHorario,title)
        }
        binding.fabAdicionaTodo.setOnClickListener{
            userTasksViewModel.addUserTask()
        }
        binding.txtTitulo.afterTextChanged {
            userTasksViewModel.getContextImage(it)
        }

    }


}