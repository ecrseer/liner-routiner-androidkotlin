package br.infnet.dk_tp1.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.infnet.dk_tp1.domain.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
        //@Singleton
        @Volatile
        private var INSTANCE: AppDatabase? = null


        fun getDatabase(context:Context,scope: CoroutineScope):AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "db_tarefahorario.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(AppHorarioTarefaDatabaseCallback(scope)).build()
                INSTANCE = instance;

                return instance

            }
        }
        private class AppHorarioTarefaDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.getHorarioDAO(),
                            database.getTarefaDAO())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        suspend fun populateDatabase(horarioDAO: DaoHorario,tarefaDAO:DaoTarefa) {
            for(horario in PopulateDatabase.CONST_HORARIOS){
                horarioDAO.inserir(horario)
            }
            for(tarefa in PopulateDatabase.CONST_TAREFAS){
                tarefaDAO.inserir(tarefa)
            }

        }
    }

}