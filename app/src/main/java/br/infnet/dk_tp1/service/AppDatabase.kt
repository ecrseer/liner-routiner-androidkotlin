package br.infnet.dk_tp1.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.infnet.dk_tp1.domain.*
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Tarefa::class,Horario::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getHorarioAndTarefaDAO(): DaoHorarioAndTarefa
    abstract fun getHorarioDAO(): DaoHorario
    abstract fun getTarefaDAO(): DaoTarefa

    companion object {
        //Singleton
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context:Context,scope: CoroutineScope):AppDatabase{
            val instance = Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java,"db_tarefahorario.db")
                .fallbackToDestructiveMigration()
                //.addCallback()
                .build()
            INSTANCE = instance;

            return instance
        }
    }

}