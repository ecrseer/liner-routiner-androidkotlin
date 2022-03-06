package br.infnet.dk_tp1.ui.main

import androidx.lifecycle.*
import br.infnet.dk_tp1.domain.Horario
import br.infnet.dk_tp1.domain.HorarioAndTarefa
import br.infnet.dk_tp1.domain.Tarefa
import br.infnet.dk_tp1.service.HorarioAndTarefaRepository
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class MainViewModel
    (private val horarioAndTarefaRepository: HorarioAndTarefaRepository) : ViewModel() {
    // TODO: Implement the ViewModel
    val tarefas =
        horarioAndTarefaRepository.getAllTarefasLiveData().asLiveData()
    val horarios: LiveData<List<Horario>> =
        horarioAndTarefaRepository.getAllHorariosLiveData().asLiveData()
    val horarioAndTarefas: LiveData<List<HorarioAndTarefa>> =
        horarioAndTarefaRepository.getTodosHorariosAndTarefasLiveData().asLiveData()



    fun gravarRotinasEmArquivo(arquivo: File) {
        viewModelScope.launch {
            val stream = FileOutputStream(arquivo)
            var rotinaTxt = "exemplo"
            horarioAndTarefas.value?.forEach {
                val temTarefa = it.tarefa
                rotinaTxt+= """
                    -------------------\n
                    Horario ${it.horario.inicio}:00 as ${it.horario.fim}:00 
                    
                """.trimIndent()
                temTarefa?.let{tarefa ->
                    rotinaTxt+= "${tarefa.nome}, ${tarefa.descricao}\n"
                }
            }
            stream.write(rotinaTxt.toByteArray())
            stream.close()
        }

    }
    fun encontraHorarioPorTarefaId(tarefaId:Long): Horario? {
        horarioAndTarefas?.value?.let{
            for(horarioEtarefa in horarioAndTarefas.value!!){
                if(horarioEtarefa.tarefa.idTarefa==tarefaId)
                    return horarioEtarefa.horario
            }
        }
        return null
    }

    fun limparTarefa(idTarefa:Long){
        viewModelScope.launch {
            val horario = encontraHorarioPorTarefaId(idTarefa)!!
            val tarefaLimpa = Tarefa(idTarefa,"","",horario.idHorario)
            horarioAndTarefaRepository.limparTarefa(tarefaLimpa)
        }
    }

}