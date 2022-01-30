package br.infnet.dk_tp1.domain

import androidx.room.*
import java.util.*

@Entity
data class Tarefa(
    @PrimaryKey(autoGenerate = true)
    val idTarefa:Long,
    val nome:String,
    val descricao:String,
    val horarioId:Long,
)

@Entity
data class Horario(
    @PrimaryKey(autoGenerate = true)
    val idHorario:Long,
    val inicio:Int,
    val fim:Int,
)


data class HorarioAndTarefa(
    @Embedded
    val horario:Horario,
    @Relation(parentColumn = "idHorario",entityColumn = "horarioId")
    val tarefa:Tarefa

)
