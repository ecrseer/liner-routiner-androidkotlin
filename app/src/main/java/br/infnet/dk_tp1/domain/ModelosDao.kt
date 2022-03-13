package br.infnet.dk_tp1.domain

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
 interface DaoRoutine {
    @Query("SELECT * FROM routine")
    fun loadRoutinesWithHorarios():List<RoutineWithHorario>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserir(routine: Routine): Long

    @Query("SELECT * FROM routine")
    fun listar(): Flow<List<Routine>>

    @Update
    suspend fun editar(routine: Routine):Int
}

@Dao
interface DaoHorarioAndTarefa {
    @Transaction
    //@Query("SELECT * FROM horario")
    @Query("SELECT horario.*, tarefa.* FROM horario INNER JOIN tarefa ON horario.idHorario = tarefa.idTarefa")
    fun getHorariosAndTarefas(): Flow<List<HorarioAndTarefa>>
}

@Dao
interface DaoHorario {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserir(horario: Horario): Long

    @Query("SELECT * FROM horario")
    fun listar(): Flow<List<Horario>>
    @Update
    suspend fun editar(horario: Horario):Int
}

@Dao
interface DaoTarefa {
    @Query(value = "SELECT * FROM tarefa WHERE idTarefa = :id")
    suspend fun obter(id:Long):Tarefa

    @Query(value = "SELECT * FROM tarefa WHERE idTarefa = :id")
      fun obterLiveData(id:Long):LiveData<Tarefa>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserir(tarefa: Tarefa): Long

    @Update
    suspend fun editar(tarefa: Tarefa):Int

    @Query("SELECT * FROM tarefa")
    fun listar(): Flow<List<Tarefa>>
}
