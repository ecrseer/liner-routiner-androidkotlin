package br.infnet.dk_tp1.ui.tarefa

import androidx.lifecycle.*
import br.infnet.dk_tp1.domain.MicroTarefa
import br.infnet.dk_tp1.domain.Tarefa
import br.infnet.dk_tp1.service.TarefaRepository
import kotlinx.coroutines.launch

class TarefaViewModel(private val tarefaRepository: TarefaRepository,private val idTarefa:Long) : ViewModel() {
    // TODO: Implement the ViewModel

      var tarefa:LiveData<Tarefa> = tarefaRepository.getTarefaByIdLiveData(idTarefa)
            //tarefa.postValue(tarefaRepository.getTarefaById(idTarefa))
    val status=MutableLiveData<String>().apply{value=""}

    val microTarefas = MutableLiveData<MutableList<MicroTarefa>>(
        mutableListOf(
            MicroTarefa(1L, "comer"),
            MicroTarefa(2L, "beber")
        )
    )
    fun adicionarMicrotarefa(){
        val novalista = microTarefas.value
            novalista?.add(MicroTarefa(1L, ""))
        microTarefas.postValue(novalista!!)
    }

    fun editarTarefa(novoTitulo:String){
        tarefa.value?.let{
            var clone = Tarefa(it.idTarefa,novoTitulo,it.descricao,it.horarioId)

            viewModelScope.launch {
                val idMod = tarefaRepository.modificarTarefa(clone)
                val d = idMod
            }
        }
    }

}