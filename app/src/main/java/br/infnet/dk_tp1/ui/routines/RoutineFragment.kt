package br.infnet.dk_tp1.ui.routines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.infnet.dk_tp1.LinerRoutinerApplication
import br.infnet.dk_tp1.R
import br.infnet.dk_tp1.databinding.FragmentRoutineListBinding
import br.infnet.dk_tp1.ui.routines.placeholder.PlaceholderContent

/**
 * A fragment representing a list of Items.
 */
class RoutineFragment : Fragment() {

    private var columnCount = 1

    private lateinit var binding: FragmentRoutineListBinding
    lateinit var viewModel: RoutineViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_routine_list, container, false)


        val linerApp = requireActivity().application as LinerRoutinerApplication
        val factory = RouteViewModelFactory(linerApp.routinesRepository)
        viewModel = ViewModelProvider(this, factory).get(RoutineViewModel::class.java)

        binding = FragmentRoutineListBinding.inflate(inflater, container, false)
        return binding.root

        //return inflater.inflate(R.layout.fragment_loggedin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.userRoutines.observe(viewLifecycleOwner, Observer {
            it?.let {
                with(binding.list) {
                    adapter=MyRoutineRecyclerViewAdapter (it,{})
                    //adapter = MicroTarefasRecyclerViewAdapter(it,{nmber->nmber})

                }
            }
        })
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