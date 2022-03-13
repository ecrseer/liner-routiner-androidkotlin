package br.infnet.dk_tp1.domain

object PopulateDatabase {
    val CONST_HORARIOS =  listOf(
        Horario(1, 6, 7),
        Horario(2, 7, 8),
        Horario(3, 8, 9),
        Horario(4, 9, 10),
        Horario(5, 10, 11),
        Horario(6, 11, 12),

        Horario(7, 12, 13),
        Horario(8, 13, 14),
        Horario(9, 14, 15),
        Horario(10, 15, 16),
        Horario(11, 16, 17),
        Horario(12, 17, 18),

        Horario(13, 18, 19),
        Horario(14, 19, 20),
        Horario(15, 20, 21),
        Horario(16, 21, 22),
        Horario(17, 22, 23),
        Horario(18, 23, 24)        )
      val CONST_HORARIOS_WITH_USERTASKS = { routineId:Long->
          val emptyUserTasks = mutableListOf("")
          listOf(
              Horario(1, 6, 7,routineId,emptyUserTasks),
              Horario(2, 7, 8,routineId,emptyUserTasks),
              Horario(3, 8, 9,routineId,emptyUserTasks),
              Horario(4, 9, 10,routineId,emptyUserTasks),
              Horario(5, 10, 11,routineId,emptyUserTasks),
              Horario(6, 11, 12,routineId,emptyUserTasks),

              Horario(7, 12, 13,routineId,emptyUserTasks),
              Horario(8, 13, 14,routineId,emptyUserTasks),
              Horario(9, 14, 15,routineId,emptyUserTasks),
              Horario(10, 15, 16,routineId,emptyUserTasks),
              Horario(11, 16, 17,routineId,emptyUserTasks),
              Horario(12, 17, 18,routineId,emptyUserTasks),

              Horario(13, 18, 19,routineId,emptyUserTasks),
              Horario(14, 19, 20,routineId,emptyUserTasks),
              Horario(15, 20, 21,routineId,emptyUserTasks),
              Horario(16, 21, 22,routineId,emptyUserTasks),
              Horario(17, 22, 23,routineId,emptyUserTasks),
              Horario(18, 23, 24,routineId,emptyUserTasks)
          )
      }
}