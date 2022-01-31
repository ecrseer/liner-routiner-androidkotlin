package br.infnet.dk_tp1.ui.main

import androidx.lifecycle.*
import br.infnet.dk_tp1.domain.Horario
import br.infnet.dk_tp1.domain.HorarioAndTarefa
import br.infnet.dk_tp1.domain.Tarefa
import br.infnet.dk_tp1.service.HorarioAndTarefaRepository
import kotlinx.coroutines.launch

class MainViewModel
    (private val horarioAndTarefaRepository: HorarioAndTarefaRepository): ViewModel() {
    // TODO: Implement the ViewModel
    val tarefas= MutableLiveData<List<Tarefa>>(
        mutableListOf(Tarefa(1,"comer","",1),
            Tarefa(2,"beber","",2))
    )
    val horarios:LiveData<List<Horario>> = horarioAndTarefaRepository.getAllHorariosLiveData().asLiveData()
    val horarioAndTarefas:LiveData<List<HorarioAndTarefa>> = horarioAndTarefaRepository.getTodosHorariosAndTarefasLiveData().asLiveData()

    fun popularHorarios(){
        val horarios = listOf(
            Horario(0,7,8),
            Horario(0,8,9),
            Horario(0,10,11),
            Horario(0,11,12),
            Horario(0,13,14),
            Horario(0,15,16),

            Horario(0,16,17),
            Horario(0,17,18),
            Horario(0,19,20),
            Horario(0,21,22),
            Horario(0,22,23),
            Horario(0,23,24),

            )
        for (horario in horarios){
            var tarefaVazia = Tarefa(0,"","",0)
            viewModelScope.launch {
                horarioAndTarefaRepository.criaHorarioAndTarefa(horario,tarefaVazia)
            }
        }
    }
    fun criar(horario: Horario){
        val l =""
        viewModelScope.launch {
            val id = horarioAndTarefaRepository.inserirHorario(horario)
            val i = 0
        }
    }

}