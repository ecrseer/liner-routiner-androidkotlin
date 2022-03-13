package br.infnet.dk_tp1.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.infnet.dk_tp1.domain.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow

class RoutineRepository(
    private val daoRoutine:DaoRoutine,
    private val daoHorario: DaoHorario
) {
        fun getAllRoutinesByUserIdFirestore(userId:String): MutableLiveData<MutableList<Routine>> {
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

        suspend fun getAllRoutines(): List<RoutineWithHorario> {
            return daoRoutine.loadRoutinesWithHorarios()
        }
        suspend fun inserirRoutine(routine: Routine): Long {
            return daoRoutine.inserir(routine)

        }
        fun getAllRoutineLiveData(): Flow<List<Routine>> {
            return daoRoutine.listar()
        }
        suspend fun modificarRoutine(routine:Routine): Int {
            return daoRoutine.editar(routine)
        }



    suspend fun inserirHorario(horario:Horario): Long {
        return daoHorario.inserir(horario)
    }

    suspend fun modificarHorario(horario: Horario): Int {
        return daoHorario.editar(horario)
    }




    fun getAllHorariosLiveData(): Flow<List<Horario>> {
        return daoHorario.listar()
    }




}

