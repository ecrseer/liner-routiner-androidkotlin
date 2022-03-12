package br.infnet.dk_tp1.ui.routines

import androidx.lifecycle.*
import br.infnet.dk_tp1.domain.Routine
import br.infnet.dk_tp1.service.RoutinesRepository

class RouteViewModelFactory (private val repository: RoutinesRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RoutineViewModel::class.java)){
            return RoutineViewModel(repository) as T;
        }
        throw IllegalArgumentException(" LoggedinViewModel instanciando errado")
    }
}
class RoutineViewModel
    (private val routinesRepository: RoutinesRepository) : ViewModel() {

    //tarefa.postValue(tarefaRepository.getTarefaById(idTarefa))
    val status=MutableLiveData<String>().apply{value=""}

    val userRoutines = MutableLiveData<MutableList<Routine>>(
        mutableListOf(
            Routine(1L, "FSDS2F"),
            Routine(2L, "DFSDSF")
        )
    )
    fun adicionarMicrotarefa(){
        val novalista = userRoutines.value
        novalista?.add(Routine(1L, ""))
        userRoutines.postValue(novalista!!)
    }

}