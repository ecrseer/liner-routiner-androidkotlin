package br.infnet.dk_tp1.ui.main

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.SeekBar
import android.widget.TimePicker
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import br.infnet.dk_tp1.LinerRoutinerApplication
import br.infnet.dk_tp1.R
import br.infnet.dk_tp1.databinding.MainFragmentBinding
import br.infnet.dk_tp1.domain.Horario
import br.infnet.dk_tp1.service.HorarioAndTarefaRepository
import androidx.lifecycle.Observer
import java.util.*

class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }
    private var _binding: MainFragmentBinding? = null
    private val binding get()= _binding!!
    //private lateinit var viewModel: MainViewModel


    inner class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            val is24 = DateFormat.is24HourFormat(activity)

            return TimePickerDialog(activity, this, hour, minute,is24 )
        }

        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {

            val sp = activity?.getPreferences(Context.MODE_PRIVATE)
            val dataMili=sp?.getLong("dataDeletarRotinas",0)
            with (sp?.edit()){
                //this?.putLong("dataDeletarRotinas",hourOfDay,minute)
            }

            onDestroy()
        }
    }
    inner class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

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
            val sp = activity?.getPreferences(Context.MODE_PRIVATE)

            with (sp?.edit()){
                //this?.putLong("dataDeletarRotinas",hourOfDay,minute)
            }
            onDestroy()
        }
    }

    inner class MainViewModelFactory (private val repository: HorarioAndTarefaRepository ): ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainViewModel::class.java)){
                return MainViewModel(repository) as T;
            }
            throw IllegalArgumentException(" ViewModel instanciando errado")
        }
    }
    private val viewModel:MainViewModel by activityViewModels {
        MainViewModelFactory(
            (requireActivity().application as LinerRoutinerApplication).horarioAndTarefaRepository
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ingr = Calendar.getInstance().timeInMillis;

        binding.rotinaTempSwitch.setOnCheckedChangeListener { buttonView, isChecked:Boolean ->
            if(isChecked){
                //todo data
                DatePickerFragment().show(childFragmentManager,"deletar em")
            }else{

            }
        }
        viewModel.horarios.observe(viewLifecycleOwner,  Observer{
            it?.let{
               if(it.size==0){ viewModel.popularHorarios() }

            }

        })
        viewModel.horarioAndTarefas.observe(viewLifecycleOwner,Observer{
            it?.let{
                val d = it
            }
        })
        setupViewPagerComSeekbar()
    }

    private fun setupViewPagerComSeekbar() {

        with (binding.seekBar as SeekBar){
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    binding.viewpgr.currentItem = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    //TODO("Not yet implemented")
                }
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    //TODO("Not yet implemented")
                }

            })
        }
        fun sincronizaHorarioTxt(position:Int){
            viewModel.horarioAndTarefas?.value?.let{
                it?.get(position)?.let{
                    val horaInicio = "${it.horario.inicio}:00"
                    val horaFim = "${it.horario.fim}:00"

                    binding.horaSelecionada.setText(horaInicio)
                    binding.txtHoraFim.setText(horaFim)
                }
            }
        }
        with(binding.viewpgr as ViewPager2){

            viewModel?.tarefas?.value?.size?.let{
                adapter=SliderAdapter(childFragmentManager,lifecycle,it)
            }

            this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.seekBar.progress = position
                    sincronizaHorarioTxt(position)
                }
            })
            setupItemTouchPraCima(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = MainFragmentBinding.inflate(inflater,container,false)
        //viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val viw = binding.root
        return viw

    }

    private fun setupItemTouchPraCima(viewPager2: ViewPager2) {
        viewPager2.children.find { it is RecyclerView }?.let {
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    //todo deletar

                }
            }).attachToRecyclerView(it as RecyclerView)

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}