package br.infnet.dk_tp1.service

import android.content.Context
import androidx.room.*
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
@Database(entities = [Routine::class,Horario::class], version = 1, exportSchema = false)
@TypeConverters(DbConverters::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getRoutineDAO(): DaoRoutine
    abstract fun getHorarioDAO(): DaoHorario

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null


        fun getDatabase(context:Context,scope: CoroutineScope):AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "db_tarefahorario.db"
                )
                    .fallbackToDestructiveMigration()
                    //.addCallback(AppHorarioTarefaDatabaseCallback(scope))
                    .build()
                INSTANCE = instance;

                return instance

            }
        }
    }

}