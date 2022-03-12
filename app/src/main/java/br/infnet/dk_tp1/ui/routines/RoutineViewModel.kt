package br.infnet.dk_tp1.ui.routines

import androidx.lifecycle.*
import br.infnet.dk_tp1.domain.PopulateDatabase
import br.infnet.dk_tp1.domain.Routine
import br.infnet.dk_tp1.service.HorarioAndTarefaRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class RouteViewModelFactory(private val repository: HorarioAndTarefaRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoutineViewModel::class.java)) {
            return RoutineViewModel(repository) as T;
        }
        throw IllegalArgumentException(" LoggedinViewModel instanciando errado")
    }
}

class RoutineViewModel
    (private val repository: HorarioAndTarefaRepository) : ViewModel() {


    //tarefa.postValue(tarefaRepository.getTarefaById(idTarefa))
    val status = MutableLiveData<String>().apply { value = "" }
    val userRoutines = repository.getAllRoutineLiveData().asLiveData()
    val userRoutine = MutableLiveData<MutableList<Routine>>(
        mutableListOf(
            Routine(1L, "FSDS2F"),
            Routine(2L, "DFSDSF")
        )
    )
    val lastRoutineIdAdded = MutableLiveData<Long>()
    val lastRoutineAdded = MutableLiveData<Routine>()

    fun addRoutine() {
        viewModelScope.launch {
            val now = Calendar.getInstance().timeInMillis
            val routine = Routine(null, "$now", listOf(""))
            val task = async { repository.inserirRoutine(routine) }
            val idRoutine = task.await()
            routine.idRoutine = idRoutine
            lastRoutineIdAdded.postValue(idRoutine)
            lastRoutineAdded.postValue(routine)
        }
    }


    fun createRoutineOnFirestore(userId: String,routine:Routine): Int {

            val routines = Firebase.firestore
                .collection("users").document(userId)
                .collection("routines")

            val task = routines.add(routine)
            task.addOnSuccessListener {
                    addedroutine->
                addedroutine.get().addOnSuccessListener {
                    val routine =   it.toObject(Routine::class.java)
                    viewModelScope.launch {
                        if (routine != null) {
                            repository.modificarRoutine(routine)
                        }
                    }
                    createRoutineHorarios(addedroutine,routines)
                }

            }

        return 1
    }

    private fun createRoutineHorarios(routine: DocumentReference?, routines: CollectionReference) {
        viewModelScope.launch {
            if (routine != null) {

                val horarios = routine.collection("horarios")
                for(horario in PopulateDatabase.CONST_HORARIOS){
                    horarios.add(horario)
                }
                createRoutineTarefas(routine,routines)
            }
        }


    }
    private fun createRoutineTarefas(routine: DocumentReference?, routines: CollectionReference) {
        //viewmodelscope
        if (routine != null) {
            val tarefas = routine.collection("tarefas")
            for(tarefa in PopulateDatabase.CONST_TAREFAS){
                tarefas.add(tarefa)
            }
        }
    }

}