package br.infnet.dk_tp1.service

import android.app.Application
import br.infnet.dk_tp1.domain.DaoHorario
import br.infnet.dk_tp1.domain.DaoHorarioAndTarefa
import br.infnet.dk_tp1.domain.Horario

class HorarioAndTarefaRepository(
    private val daoHorarioTarefa: DaoHorarioAndTarefa,
    private val daoHorario: DaoHorario
) {
    suspend fun inserirHorario(horario:Horario): Long {
        return daoHorario.inserir(horario)
    }

}

class TarefaRepository(

) {

}