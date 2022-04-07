package br.infnet.dk_tp1.domain

import androidx.annotation.NonNull
import androidx.room.*
import com.google.firebase.firestore.DocumentId
import java.util.*

@Entity
data class Routine(
    @PrimaryKey(autoGenerate = true)
    var idRoutine: Long? = null,
    val name:String?=null,
    val horariosIdsFirebase: List<String>? = null,
    @DocumentId
    val firestoreIdRoutine:String?=null
)



@Entity
data class Horario(
    @PrimaryKey(autoGenerate = true)
    val idHorario: Long? = null,
    val inicio: Int? = null,
    val fim: Int? = null,
    val routineId: Long? = null,
    var userTasks:List<String>?=null,
    var title:String?=null,
    var imgUrlTitle:String?=null,
    @DocumentId
    val firestoreIdHorario:String?=null
)

data class RoutineWithHorario(
    @Embedded
    val routine: Routine,

    @Relation(
        parentColumn = "idRoutine",
        entityColumn = "routineId", entity = Horario::class)

    var horarios: List<Horario>
)

