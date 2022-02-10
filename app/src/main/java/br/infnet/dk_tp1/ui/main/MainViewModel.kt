package br.infnet.dk_tp1.ui.main

import androidx.lifecycle.*
import br.infnet.dk_tp1.domain.Horario
import br.infnet.dk_tp1.domain.HorarioAndTarefa
import br.infnet.dk_tp1.domain.Tarefa
import br.infnet.dk_tp1.service.HorarioAndTarefaRepository
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel
    (private val horarioAndTarefaRepository: HorarioAndTarefaRepository) : ViewModel() {
    // TODO: Implement the ViewModel
    val tarefas =
        horarioAndTarefaRepository.getAllTarefasLiveData().asLiveData()
    val horarios: LiveData<List<Horario>> =
        horarioAndTarefaRepository.getAllHorariosLiveData().asLiveData()
    val horarioAndTarefas: LiveData<List<HorarioAndTarefa>> =
        horarioAndTarefaRepository.getTodosHorariosAndTarefasLiveData().asLiveData()

    val microTarefas = MutableLiveData<List<Tarefa>>(
        mutableListOf(
            Tarefa(1, "comer", "", 1),
            Tarefa(2, "beber", "", 2)
        )
    )

    fun recordFile(file: File) {
        viewModelScope.launch {

            file.createNewFile()

        }

    }

    fun criar(horario: Horario) {
        val l = ""
        viewModelScope.launch {
            val id = horarioAndTarefaRepository.inserirHorario(horario)
            val i = 0
        }
    }

}