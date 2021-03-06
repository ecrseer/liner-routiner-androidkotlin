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
interface DaoHorario {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserir(horario: Horario): Long

    @Query("SELECT * FROM horario")
    fun listar(): Flow<List<Horario>>
    @Update
    suspend fun editar(horario: Horario):Int
}


