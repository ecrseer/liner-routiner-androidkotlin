package br.infnet.dk_tp1.ui.main

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
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
import br.infnet.dk_tp1.databinding.MainFragmentBinding
import br.infnet.dk_tp1.service.HorarioAndTarefaRepository
import androidx.lifecycle.Observer
import br.infnet.dk_tp1.ui.dialogs.MeuDatePickerDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.coroutineScope
import java.io.File
import java.io.FileOutputStream
import java.util.*

class MainFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    //private lateinit var viewModel: MainViewModel


    inner class MainViewModelFactory(private val repository: HorarioAndTarefaRepository) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(repository) as T;
            }
            throw IllegalArgumentException(" ViewModel instanciando errado")
        }
    }

    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(
            (requireActivity().application as LinerRoutinerApplication).horarioAndTarefaRepository
        )
    }

    private fun escutarSwitchRotinaTemporaria() {
        binding.rotinaTempSwitch.setOnCheckedChangeListener { buttonView, isChecked: Boolean ->
            var dataSelecionada: Date
            if (isChecked) {
                val setDataSelecionada = { ano: Int, mes: Int, dia: Int ->
                    var data = Date(ano, mes, dia)
                    dataSelecionada = data
                }
                //tododata
                MeuDatePickerDialog(setDataSelecionada).show(childFragmentManager, "datadata")

            } else {
                val thisPreferences = activity?.getPreferences(Context.MODE_PRIVATE)
                with(thisPreferences?.edit()) {
                    this?.putString("remover_em", "");
                }


            }
        }
    }

    fun openDirectory(pickerInitialUri: Uri) {
        val rcode = 32
        // Choose a directory using the system's file picker.
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            // Optionally, specify a URI for the directory that should be opened in
            // the system file picker when it loads.
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }

        startActivityForResult(intent, rcode)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGravar.setOnClickListener { 
            val arquivo = File(requireContext()
                .getExternalFilesDir(Environment.DIRECTORY_DCIM),"rotina.txt" )
            viewModel.gravarRotinasEmArquivo(arquivo)
            Snackbar.make(it,"Gravando sua rotina em ${arquivo.absolutePath}",Snackbar.LENGTH_LONG+4242).show()
        }



        setupViewPagerComSeekbar()
        escutarSwitchRotinaTemporaria()
        deletaRotinaSeTemporaria()
    }

    private fun deletaRotinaSeTemporaria() {
        //val txt = requireContext().openFileInput("remover_em").bufferedReader().readText()

    }

    private fun setupViewPagerComSeekbar() {

        with(binding.seekBar as SeekBar) {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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

        with(binding.viewpgr as ViewPager2) {
            viewModel.tarefas.observe(viewLifecycleOwner, Observer {
                it?.size?.let {
                    adapter = SliderAdapter(childFragmentManager, lifecycle, it)
                }
            })
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

    private fun sincronizaHorarioTxt(position: Int) {
        viewModel.horarioAndTarefas?.value?.let {
            if (it?.size > 0) {
                it.get(position)?.let {
                    val horaInicio = "${it.horario.inicio}:00"
                    val horaFim = "${it.horario.fim}:00"

                    binding.horaSelecionada.setText(horaInicio)
                    binding.txtHoraFim.setText(horaFim)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.horarioAndTarefas.observe(viewLifecycleOwner, Observer {
             sincronizaHorarioTxt(0)
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = MainFragmentBinding.inflate(inflater, container, false)

        viewModel.horarios.observe(viewLifecycleOwner, Observer {

        })
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

                    val supostoIdTarefa = viewHolder.adapterPosition+1
                    viewModel.limparTarefa(supostoIdTarefa.toLong())


                }
            }).attachToRecyclerView(it as RecyclerView)

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        //TODO("Not yet implemented")
        val yea = year
    }


}