package br.infnet.dk_tp1.ui.main

import androidx.lifecycle.*
import br.infnet.dk_tp1.domain.Horario
import br.infnet.dk_tp1.service.RoutineRepository
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class MainViewModel
    (
    private val routineRepository: RoutineRepository,
    userId: String,
    routineId: String
) : ViewModel() {
    // TODO: Implement the ViewModel

    val horarios: LiveData<List<Horario>> =
        routineRepository.getAllHorariosLiveData().asLiveData()

   val routine = Firebase.firestore
        .collection("users").document(userId)
        .collection("routines").document(routineId)

    val horarios2 = MutableLiveData<List<Horario>> ()


    fun loadFsData(){
        val asc = Query.Direction.ASCENDING
        if(horarios2.value.isNullOrEmpty() ){
            routine.collection("horarios")
                .orderBy("inicio",asc).get()
                .addOnSuccessListener {snapshot->
                val list = snapshot.toObjects<Horario>()
                horarios2.postValue(list)

            }
        }
    }

    fun gravarRotinasEmArquivo(arquivo: File) {
        viewModelScope.launch {
            val stream = FileOutputStream(arquivo)
            var rotinaTxt = "exemplo"
            horarios2.value?.forEach {
                val userTasks = it.userTasks
                rotinaTxt += """
                    -------------------\n
                    Horario ${it.inicio}:00 as ${it.fim}:00 
                    
                """.trimIndent()
                if (userTasks != null) {
                    for (task in userTasks){
                        rotinaTxt += "${task} \n"
                    }
                }
            }
            stream.write(rotinaTxt.toByteArray())
            stream.close()
        }

    }

    fun encontraHorarioPorTarefaId(tarefaId: Long): Horario? {
        /*horarioAndTarefas?.value?.let {
            for (horarioEtarefa in horarioAndTarefas.value!!) {
                if (horarioEtarefa.tarefa.idTarefa == tarefaId)
                    return horarioEtarefa.horario
            }
        }*/
        return null
    }

    fun limparTarefa(idTarefa: Long) {
       /* viewModelScope.launch {
            val horario = encontraHorarioPorTarefaId(idTarefa)!!
            val tarefaLimpa = Tarefa(idTarefa, "", "", horario.idHorario)
            routineRepository.limparTarefa(tarefaLimpa)
        }*/
    }

    val userTasks = MutableLiveData<MutableList<String>>()


    fun editarHorario(newUserTasks: MutableList<String>?, horarioPosition:Long) {
        val horario = horarios2.value?.get(horarioPosition.toInt())
        horario?.let { horarioInDb ->
            horarioInDb.userTasks = newUserTasks
            viewModelScope.launch { routineRepository.modificarHorario(horarioInDb) }
            horarioInDb.firestoreIdHorario?.let { idHorarioFs ->
                routine.collection("horarios")
                .document(idHorarioFs).set(horarioInDb)
            }

        }
    }

}