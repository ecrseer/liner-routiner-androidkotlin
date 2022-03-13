package br.infnet.dk_tp1

import android.app.Application
import br.infnet.dk_tp1.service.AppDatabase
import br.infnet.dk_tp1.service.RoutineRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class LinerRoutinerApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob() )

    val database by lazy { AppDatabase.getDatabase(this,applicationScope)}


    val routineRepository by lazy { RoutineRepository(
        database.getRoutineDAO(),
        database.getHorarioDAO(),
    ) }



}