package br.infnet.dk_tp1.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
data class Tarefa(
    val id:Int,
    val nome:String
)
class MainViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val tarefas= MutableLiveData<List<Tarefa>>(
        mutableListOf(Tarefa(1,"comer"),Tarefa(2,"beber"))
    )

}