package br.infnet.dk_tp1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.infnet.dk_tp1.LinerRoutinerApplication
import br.infnet.dk_tp1.databinding.FragmentLoggedinBinding
import br.infnet.dk_tp1.ui.tarefa.MicroTarefasRecyclerViewAdapter
import br.infnet.dk_tp1.ui.tarefa.TarefaViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [LoggedinFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoggedinFragment : Fragment() {
    private val ARG_PARAM1 = "param1"

    // TODO: Rename and change types of parameters
    private var param1: String? = null


    private lateinit var binding:FragmentLoggedinBinding

    val activityViewModel: MainActivityViewModel by activityViewModels()
    lateinit var viewModel: LoggedinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val linerApp = requireActivity().application as LinerRoutinerApplication
        val factory = LoggedinViewModelFactory(linerApp.routinesRepository)
        viewModel = ViewModelProvider(this,factory).get(LoggedinViewModel::class.java)

        binding = FragmentLoggedinBinding.inflate(inflater,container,false)
        return binding.root
        //return inflater.inflate(R.layout.fragment_loggedin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.userRoutines.observe(viewLifecycleOwner, Observer {
            it?.let{
                with (binding.routinesRv){
                    adapter = MicroTarefasRecyclerViewAdapter(it,{nmber->nmber})
                }
            }
        })
    }


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
        fun newInstance(param1: String) =
            LoggedinFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}