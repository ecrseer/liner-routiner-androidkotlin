package br.infnet.dk_tp1.service

import androidx.lifecycle.LiveData
import br.infnet.dk_tp1.domain.*
import kotlinx.coroutines.flow.Flow

class HorarioAndTarefaRepository(
    private val daoHorarioTarefa: DaoHorarioAndTarefa,
    private val daoHorario: DaoHorario,
    private val daoTarefa:DaoTarefa
) {
    suspend fun inserirHorario(horario:Horario): Long {
        return daoHorario.inserir(horario)
    }
    suspend fun inserirTarefa(tarefa:Tarefa):Long{
        return daoTarefa.inserir(tarefa)
    }

    suspend fun criaHorarioAndTarefa(horario:Horario, tarefaSemIds: Tarefa): Long {
        val idHorario = inserirHorario(horario)
        var tarefaClone = tarefaSemIds.cloneComIdDiferente(idHorario)
        return inserirTarefa(tarefaClone)
    }

    fun getAllHorariosLiveData(): Flow<List<Horario>> {
        return daoHorario.listar()
    }
    fun getTodosHorariosAndTarefasLiveData():Flow<List<HorarioAndTarefa>>{
        return daoHorarioTarefa.getHorariosAndTarefas()
    }

}

class TarefaRepository(
    private val daoTarefa:DaoTarefa
) {
    suspend fun modificarTarefa(tarefa:Tarefa): Int {
        return daoTarefa.editar(tarefa)
    }
    suspend fun getTarefaById(id:Long): Tarefa {
        return daoTarefa.obter(id)
    }
     fun getTarefaByIdLiveData(id:Long): LiveData<Tarefa> {
        return daoTarefa.obterLiveData(id)
    }
}