package br.infnet.dk_tp1.ui.tarefa

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.infnet.dk_tp1.domain.Horario
import br.infnet.dk_tp1.domain.Tarefa

class UserTasksViewModelFactory(
    private val horarios: List<Horario>,
    private val idUserTask: Long
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserTasksViewModel::class.java)) {
            return UserTasksViewModel(horarios, idUserTask) as T;
        }
        throw IllegalArgumentException(" TarefaViewModel instanciando errado")
    }
}

class UserTasksViewModel(private val horarios: List<Horario>, private val idUserTask: Long) :
    ViewModel() {
    // TODO: Implement the ViewModel

    var tarefa = MutableLiveData<MutableList<Tarefa>>()

    //tarefa.postValue(tarefaRepository.getTarefaById(idUserTask))
    val status = MutableLiveData<String>().apply { value = "" }

    val userTasks = MutableLiveData<MutableList<String>>()

    init {
        val utasks = horarios.get(idUserTask.toInt()).userTasks
        val mutableUtasks: MutableList<String>? = utasks?.toMutableList()
        mutableUtasks?.let {
            userTasks.postValue(it)
        }
    }

    fun addUserTask() {
        val clone = userTasks.value
        clone?.add("")
        userTasks.postValue(clone!!)
    }

    fun editarTarefa(novoTitulo: String) {
        /*tarefa.value?.let{
            var clone = Tarefa(it.idUserTask,novoTitulo,it.descricao,it.horarioId)

            viewModelScope.launch {
                val idMod = tarefaRepository.modificarTarefa(clone)
                val d = idMod
            }
        }*/
    }

}