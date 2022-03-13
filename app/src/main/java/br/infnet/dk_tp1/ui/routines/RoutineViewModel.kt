package br.infnet.dk_tp1.ui.routines

import androidx.lifecycle.*
import br.infnet.dk_tp1.domain.PopulateDatabase
import br.infnet.dk_tp1.domain.Routine
import br.infnet.dk_tp1.service.RoutineRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class RoutineViewModelFactory(private val repository: RoutineRepository,
                              private val userId: String) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoutineViewModel::class.java)) {
            return RoutineViewModel(repository,userId) as T;
        }
        throw IllegalArgumentException(" LoggedinViewModel instanciando errado")
    }
}

class RoutineViewModel
    (private val repository: RoutineRepository,private val userId: String) : ViewModel() {


    val userRoutines = repository.getAllRoutinesByUserIdFirestore(userId)
    val lastRoutineAdded = MutableLiveData<Routine>()

    fun addRoutine() {
        viewModelScope.launch {
            val routine = repository.createRoutine()
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
                    val routine = it.toObject(Routine::class.java)
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
                lastRoutineAdded.value?.let{routine->
                val constHorarios = PopulateDatabase.CONST_HORARIOS_WITH_USERTASKS(routine.idRoutine!!)
                for(horario in constHorarios){
                    horarios.add(horario)
                }

                }
            }
        }


    }

}