package br.infnet.dk_tp1.service

import androidx.lifecycle.MutableLiveData
import br.infnet.dk_tp1.domain.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import java.util.*

class RoutineRepository(
    private val daoRoutine: DaoRoutine,
    private val daoHorario: DaoHorario
) {
    companion object {
        private var _thisUserRoutines: CollectionReference? = null
        val thisUserRoutines: CollectionReference
            get() {
                if (this._thisUserRoutines == null) {
                    this._thisUserRoutines = Firebase.firestore
                        .collection("users").document("userId")
                        .collection("routines")

                }
                return this._thisUserRoutines!!
            }

    }

    fun setupRoutinesListener(
        userId: String,
        listener: EventListener<QuerySnapshot>
    ) {
        val thisUserRoutines = Firebase.firestore
            .collection("users").document(userId)
            .collection("routines")
        thisUserRoutines.addSnapshotListener(listener)

    }

    fun getAllRoutinesByUserIdFirestore(userId: String): MutableLiveData<MutableList<Routine>> {
        val getRoutinesTask = Firebase.firestore
            .collection("users").document(userId)
            .collection("routines").get()
        val routinesLiveData = MutableLiveData<MutableList<Routine>>()
        getRoutinesTask.addOnSuccessListener {
            val stored = it.toObjects<Routine>()
            routinesLiveData.postValue(stored.toMutableList())
        }
        return routinesLiveData
    }

    suspend fun createRoutine(): Routine {
        val now = Calendar.getInstance().timeInMillis
        val routine = Routine(null, "$now", listOf(""))
        val idRoutine = inserirRoutine(routine)
        routine.idRoutine = idRoutine
        return routine
    }

    suspend fun getAllRoutines(): List<RoutineWithHorario> {
        return daoRoutine.loadRoutinesWithHorarios()
    }

    suspend fun inserirRoutine(routine: Routine): Long {
        return daoRoutine.inserir(routine)

    }

    fun getAllRoutineLiveData(): Flow<List<Routine>> {
        return daoRoutine.listar()
    }

    suspend fun modificarRoutine(routine: Routine): Int {
        return daoRoutine.editar(routine)
    }


    suspend fun inserirHorario(horario: Horario): Long {
        return daoHorario.inserir(horario)
    }

    suspend fun modificarHorario(horario: Horario): Int {
        return daoHorario.editar(horario)
    }


    fun getAllHorariosLiveData(): Flow<List<Horario>> {
        return daoHorario.listar()
    }


}

