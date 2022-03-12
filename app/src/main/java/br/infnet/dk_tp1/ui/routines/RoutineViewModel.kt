package br.infnet.dk_tp1.ui.routines

import androidx.lifecycle.*
import br.infnet.dk_tp1.domain.Routine
import br.infnet.dk_tp1.service.RoutinesRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class RouteViewModelFactory(private val repository: RoutinesRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoutineViewModel::class.java)) {
            return RoutineViewModel(repository) as T;
        }
        throw IllegalArgumentException(" LoggedinViewModel instanciando errado")
    }
}

class RoutineViewModel
    (private val routinesRepository: RoutinesRepository) : ViewModel() {

    //tarefa.postValue(tarefaRepository.getTarefaById(idTarefa))
    val status = MutableLiveData<String>().apply { value = "" }
    val userRoutines = routinesRepository.getAllRoutineLiveData().asLiveData()
    val userRoutine = MutableLiveData<MutableList<Routine>>(
        mutableListOf(
            Routine(1L, "FSDS2F"),
            Routine(2L, "DFSDSF")
        )
    )
    val lastRoutineIdAdded = MutableLiveData<Long>()
    fun addRoutine() {
        viewModelScope.launch {
            val now = Calendar.getInstance().timeInMillis
            val routine = Routine(0, "$now", listOf(""))
            var idRoutine: Long = 0
            val task = async { routinesRepository.inserirRoutine(routine) }
            idRoutine = task.await()
            routine.idRoutine = idRoutine
            lastRoutineIdAdded.postValue(idRoutine)
        }
    }

    fun createRoutine(userId: String): Int {
        userRoutines?.value?.let {
            val lastRoutine = it.last()

            val routines = Firebase.firestore
                .collection("users").document(userId)
                .collection("routines")

            routines.add(lastRoutine)
        }
        return 1
    }

}