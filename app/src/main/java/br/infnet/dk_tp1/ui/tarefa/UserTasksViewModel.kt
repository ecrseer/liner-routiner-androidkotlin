package br.infnet.dk_tp1.ui.tarefa

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.infnet.dk_tp1.domain.Horario
import br.infnet.dk_tp1.service.SearchImageService
import br.infnet.dk_tp1.service.SearchImageServiceListener
import br.infnet.dk_tp1.service.SearchedImageURL

class UserTasksViewModelFactory(
    private val horarios: List<Horario>,
    private val idUserTask: Long
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserTasksViewModel::class.java)) {
            return UserTasksViewModel(horarios, idUserTask) as T;
        }
        throw IllegalArgumentException(" TarefaViewModel instanciando errado")
    }
}

class UserTasksViewModel(private val horarios: List<Horario>, private val idUserTask: Long) :
    ViewModel(), SearchImageServiceListener {

    val userTasks = MutableLiveData<MutableList<String>>()
    val service = SearchImageService()
    init {
        val utasks = horarios.get(idUserTask.toInt()).userTasks
        val mutableUtasks: MutableList<String>? = utasks?.toMutableList()
        mutableUtasks?.let {
            userTasks.postValue(it)
        }
    }

    fun addUserTask() {
        val clone = userTasks.value
        clone?.add("")
        userTasks.postValue(clone!!)
    }

    fun modifyUserTaskByPosition(posicao: Int, newUserTaskText: String) {
        val clone = userTasks.value
        clone?.let { userTask ->
            val oldTask = userTask.get(posicao)
            if (!oldTask.equals(newUserTaskText)) {
                clone?.set(posicao, newUserTaskText)
            }
        }
    }

    val userTaskBackgroundUrl = MutableLiveData<String>()
    fun getContextImage(txt:String){
        service.getImage(txt)
    }

    override fun whenGetImageFinished(imgurl: SearchedImageURL?) {
        userTaskBackgroundUrl.value = "${imgurl?.big}"
    }

    override fun whenHttpError(erro: String) {
        println(erro)
        //TODO("Not yet implemented")
    }


}