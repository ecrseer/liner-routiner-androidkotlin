package br.infnet.dk_tp1.ui.main

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.SeekBar
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import br.infnet.dk_tp1.LinerRoutinerApplication
import br.infnet.dk_tp1.databinding.MainFragmentBinding
import br.infnet.dk_tp1.service.HorarioAndTarefaRepository
import br.infnet.dk_tp1.ui.MainActivityViewModel
import br.infnet.dk_tp1.ui.dialogs.MeuDatePickerDialog
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.util.*

class MainFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private val ARG_PARAM1 = "userId"

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoggedinFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(idUser: String) =
            MainFragment()/*.apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, idUser)
                }
            }*/
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    //private lateinit var viewModel: MainViewModel
    val args: MainFragmentArgs by navArgs()

    val activityViewModel: MainActivityViewModel by activityViewModels()

    inner class MainViewModelFactory(
        private val repository: HorarioAndTarefaRepository,
        private val userId: String,
        private val routineId: String
    ) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(repository, userId, routineId) as T;
            }
            throw IllegalArgumentException(" ViewModel instanciando errado")
        }
    }

    private val viewModel: MainViewModel by viewModels {
        val app = requireActivity().application as LinerRoutinerApplication

        MainViewModelFactory(
            app.horarioAndTarefaRepository,
            activityViewModel.mUserLiveData.value!!.uid,
            args.routineId
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

        viewModel.loadFsData()
        binding.btnGravar.setOnClickListener {
            val arquivo = File(
                requireContext()
                    .getExternalFilesDir(Environment.DIRECTORY_DCIM), "rotina.txt"
            )
            viewModel.gravarRotinasEmArquivo(arquivo)
            Snackbar.make(
                it,
                "Gravando sua rotina em ${arquivo.absolutePath}",
                Snackbar.LENGTH_LONG + 4242
            ).show()
        }

        viewModel.horarios2.observe(viewLifecycleOwner, Observer {
            it?.let{
                println(it)

            }
        })

        setupViewPagerComSeekbar()
        escutarSwitchRotinaTemporaria()
        deletaRotinaSeTemporaria()
        binding.seekBar.progress =0
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
                    println("progress $progress")
                    binding.viewpgr.currentItem = progress
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            })
        }

        with(binding.viewpgr as ViewPager2) {
            viewModel.horarios2.observe(viewLifecycleOwner, Observer {
                it?.size?.let {
                    println("size $it")
                    adapter = SliderAdapter(childFragmentManager, lifecycle, it)
                    binding.seekBar.max = it - 1
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
        viewModel.horarios2?.value?.let {
            if (it?.size > 0) {
                it.get(position)?.let {
                    val horaInicio = "${it.inicio}:00"
                    val horaFim = "${it.fim}:00"

                    binding.horaSelecionada.setText(horaInicio)
                    binding.txtHoraFim.setText(horaFim)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.horarios2.observe(viewLifecycleOwner, Observer {
            sincronizaHorarioTxt(0)
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = MainFragmentBinding.inflate(inflater, container, false)


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

                    val supostoIdTarefa = viewHolder.adapterPosition + 1
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