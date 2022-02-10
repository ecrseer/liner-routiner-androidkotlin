package br.infnet.dk_tp1.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.infnet.dk_tp1.domain.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

/**
 * Utility method to run blocks on a dedicated background thread, used for io/database work.
 */
fun ioThread(f : () -> Unit) {
    IO_EXECUTOR.execute(f)
}
@Database(entities = [Tarefa::class,Horario::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getHorarioAndTarefaDAO(): DaoHorarioAndTarefa
    abstract fun getHorarioDAO(): DaoHorario
    abstract fun getTarefaDAO(): DaoTarefa

    companion object {
        //Singleton
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context:Context,scope:CoroutineScope):AppDatabase=
            INSTANCE?: synchronized(this){
                INSTANCE?: getDatabase(context,scope).also{ INSTANCE = it}
            }

        fun getDatabase(context:Context,scope: CoroutineScope):AppDatabase{
            val instance = Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java,"db_tarefahorario.db")
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        scope.launch {
                            for(horario in PopulateDatabase.CONST_HORARIOS){
                                getInstance(context, scope).getHorarioDAO()
                                    .inserir(horario)
                            }
                            for(tarefa in PopulateDatabase.CONST_TAREFAS){
                                getInstance(context,scope).getTarefaDAO()
                                    .inserir(tarefa)
                            }
                        }
                        //getInstance(context).getHora
                        /*

                        trainDB.stationDao().insert(...)


                        */

                    }
                })
                //.addCallback()
                .build()
            INSTANCE = instance;

            return instance
        }
    }

}