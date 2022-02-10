package br.infnet.dk_tp1.ui.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class MeuDatePickerDialog  (val funRecebeDataSelecionada:(ano:Int,mes:Int,dia:Int)->Unit)
        : DialogFragment(), DatePickerDialog.OnDateSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current time as the default values for the picker
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // Create a new instance of TimePickerDialog and return it
            return DatePickerDialog(requireActivity(), this, year, month, day)
        }

        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            val thisPreferences = activity?.getPreferences(Context.MODE_PRIVATE)
            val data: String? = thisPreferences?.getString("remover_em", "");
            if (data == "") {
                with(thisPreferences?.edit()) {
                    this?.putString("remover_em", "20202020")
                    this?.apply()
                }
            }
            onDestroy()
        }

}