package br.infnet.dk_tp1.domain

import androidx.room.*

@Dao
interface DaoHorarioAndTarefa {
    @Transaction
    @Query("SELECT * FROM horario")
    fun getUsersAndLibraries(): List<HorarioAndTarefa>
}

@Dao
interface DaoHorario {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserir(horario: Horario): Long
}

@Dao
interface DaoTarefa {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserir(tarefa: Tarefa): Long
}
