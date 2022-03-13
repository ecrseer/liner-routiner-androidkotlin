package br.infnet.dk_tp1.service

import androidx.lifecycle.LiveData
import br.infnet.dk_tp1.domain.*
import kotlinx.coroutines.flow.Flow
class RoutinesRepository(
    private val daoRoutine:DaoRoutine,
){
    suspend fun getAllRoutines(): List<RoutineWithHorario> {
        return daoRoutine.loadRoutinesWithHorarios()
    }
    suspend fun inserirRoutine(routine: Routine): Long {
        return daoRoutine.inserir(routine)

    }
     fun getAllRoutineLiveData(): Flow<List<Routine>> {
        return daoRoutine.listar()
    }
}
class HorarioAndTarefaRepository(
    private val daoRoutine:DaoRoutine,
    private val daoHorarioTarefa: DaoHorarioAndTarefa,
    private val daoHorario: DaoHorario,
    private val daoTarefa:DaoTarefa
) {

        suspend fun getAllRoutines(): List<RoutineWithHorario> {
            return daoRoutine.loadRoutinesWithHorarios()
        }
        suspend fun inserirRoutine(routine: Routine): Long {
            return daoRoutine.inserir(routine)

        }
        fun getAllRoutineLiveData(): Flow<List<Routine>> {
            return daoRoutine.listar()
        }
        suspend fun modificarRoutine(routine:Routine): Int {
            return daoRoutine.editar(routine)
        }



    suspend fun inserirHorario(horario:Horario): Long {
        return daoHorario.inserir(horario)
    }

    suspend fun modificarHorario(horario: Horario): Int {
        return daoHorario.editar(horario)
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
    fun getAllTarefasLiveData():Flow<List<Tarefa>>{
        return daoTarefa.listar()
    }
    fun getTodosHorariosAndTarefasLiveData():Flow<List<HorarioAndTarefa>>{
        return daoHorarioTarefa.getHorariosAndTarefas()
    }
    suspend fun limparTarefa(tarefa:Tarefa): Int {
        val tarefaLimpa = Tarefa(tarefa.idTarefa,"","",tarefa.horarioId)
        return daoTarefa.editar(tarefaLimpa)
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