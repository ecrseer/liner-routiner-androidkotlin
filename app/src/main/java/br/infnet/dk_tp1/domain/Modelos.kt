package br.infnet.dk_tp1.domain

import androidx.annotation.NonNull
import androidx.room.*
import java.util.*

@Entity
data class Tarefa(
    @PrimaryKey(autoGenerate = true)
    val idTarefa:Long,
    val nome:String,
    val descricao:String,
    @NonNull
    val horarioId:Long,
){
    fun cloneComIdDiferente(horaIdNovo:Long): Tarefa {
        return Tarefa(this.idTarefa,this.nome,this.descricao,horaIdNovo)
    }
}

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
    //@Relation(parentColumn = "idHorario",entityColumn = "horarioId")
    @Embedded
    val tarefa:Tarefa

)

data class MicroTarefa(
    val idMicroTarefa: Long,
    val descricao: String)
