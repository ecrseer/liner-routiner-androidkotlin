package br.infnet.dk_tp1.ui

import androidx.lifecycle.*
import br.infnet.dk_tp1.domain.MicroTarefa
import br.infnet.dk_tp1.domain.Tarefa
import br.infnet.dk_tp1.service.RoutinesRepository
import kotlinx.coroutines.launch

class LoggedinViewModelFactory (private val repository: RoutinesRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoggedinViewModel::class.java)){
            return LoggedinViewModel(repository) as T;
        }
        throw IllegalArgumentException(" LoggedinViewModel instanciando errado")
    }
}
class LoggedinViewModel
    (private val routinesRepository: RoutinesRepository) : ViewModel() {

    //tarefa.postValue(tarefaRepository.getTarefaById(idTarefa))
    val status=MutableLiveData<String>().apply{value=""}

    val userRoutines = MutableLiveData<MutableList<MicroTarefa>>(
        mutableListOf(
            MicroTarefa(1L, "comer"),
            MicroTarefa(2L, "beber")
        )
    )
    fun adicionarMicrotarefa(){
        val novalista = userRoutines.value
        novalista?.add(MicroTarefa(1L, ""))
        userRoutines.postValue(novalista!!)
    }

}