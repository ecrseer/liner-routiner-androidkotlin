package br.infnet.dk_tp1.ui.routines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import br.infnet.dk_tp1.LinerRoutinerApplication
import br.infnet.dk_tp1.R
import br.infnet.dk_tp1.databinding.FragmentRoutineListBinding
import br.infnet.dk_tp1.domain.Routine
import br.infnet.dk_tp1.ui.MainActivityViewModel

/**
 * A fragment representing a list of Items.
 */
class RoutineFragment : Fragment() {

    private var columnCount = 1

    val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var binding: FragmentRoutineListBinding
    lateinit var viewModel: RoutineViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }
    private fun createViewModelByUserId(){
        val linerApp = requireActivity().application as LinerRoutinerApplication
        activityViewModel.mUserLiveData.value?.let{user->
            val factory = RoutineViewModelFactory(linerApp.routineRepository,user.uid)
            viewModel = ViewModelProvider(this, factory).get(RoutineViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_routine_list, container, false)
        createViewModelByUserId()
        binding = FragmentRoutineListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {

            binding.fabCreateroutine.setOnClickListener {
                addRoutine()
            }
            lastRoutineAdded.observe(viewLifecycleOwner, Observer {routine->
                routine?.let{
                    activityViewModel.mUserLiveData.value?.let {user->
                        createRoutineOnFirestore(user.uid,routine)
                    }
                }
            })
            userRoutines.observe(viewLifecycleOwner, Observer {
                it?.let {
                    updateList(it, view)
                }

            })
        }
    }

    private fun updateList(routines: List<Routine>, view: View) {

        binding.list.adapter = MyRoutineRecyclerViewAdapter(routines) { position,routineId ->
            val act = RoutineFragmentDirections
                .actionRoutineFragmentToMainFragment2(position = position,routineId = routineId)
            view.findNavController().navigate(act)
        }

    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            RoutineFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}