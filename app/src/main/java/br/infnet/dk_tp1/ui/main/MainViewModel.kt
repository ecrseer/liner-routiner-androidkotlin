package br.infnet.dk_tp1.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.infnet.dk_tp1.domain.Horario
import br.infnet.dk_tp1.domain.Tarefa
import br.infnet.dk_tp1.service.HorarioAndTarefaRepository
import kotlinx.coroutines.launch

class MainViewModel
    (private val horarioAndTarefaRepository: HorarioAndTarefaRepository): ViewModel() {
    // TODO: Implement the ViewModel
    val tarefas= MutableLiveData<List<Tarefa>>(
        mutableListOf(Tarefa(1,"comer","",1),Tarefa(2,"beber","",2))
    )
    fun criar(horario: Horario){
        viewModelScope.launch {
            horarioAndTarefaRepository.inserirHorario(horario)
        }
    }

}