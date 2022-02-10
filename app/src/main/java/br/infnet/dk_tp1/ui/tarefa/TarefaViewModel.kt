package br.infnet.dk_tp1.ui.tarefa

import androidx.lifecycle.*
import br.infnet.dk_tp1.domain.Tarefa
import br.infnet.dk_tp1.service.MeuAsyncTasker
import br.infnet.dk_tp1.service.TarefaRepository
import kotlinx.coroutines.launch

class TarefaViewModel(private val tarefaRepository: TarefaRepository,private val idTarefa:Long) : ViewModel() {
    // TODO: Implement the ViewModel

    val posicaoId = MutableLiveData<Long>().apply{ value = idTarefa}
      var tarefa:LiveData<Tarefa> =  Transformations.switchMap(posicaoId) {
              id:Long ->
                tarefaRepository.getTarefaByIdLiveData(id) }
            //tarefa.postValue(tarefaRepository.getTarefaById(idTarefa))
    val status=MutableLiveData<String>().apply{value=""}


    fun editarTarefa(novoTitulo:String){
        tarefa.value?.let{
            var clone = Tarefa(it.idTarefa,novoTitulo,it.descricao,it.horarioId)

            viewModelScope.launch {
                val idMod = tarefaRepository.modificarTarefa(clone)
                val d = idMod
            }
        }
    }
    fun editarTarefaAsync(novoTitulo:String){
        tarefa.value?.let{
            var clone = Tarefa(it.idTarefa,novoTitulo,it.descricao,it.horarioId)


            val modificar =   {
                val idMod = tarefaRepository.modificarTarefa(clone)
                status.postValue("$idMod")

              }

            MeuAsyncTasker (modificar).execute()

        }
    }

}