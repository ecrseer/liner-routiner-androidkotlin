package br.infnet.dk_tp1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import br.infnet.dk_tp1.LinerRoutinerApplication
import br.infnet.dk_tp1.R
import br.infnet.dk_tp1.databinding.FragmentLoggedinBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }
    private fun setupBottombuttonsnav(view:View) {
        binding.bottomNavigationView.setOnItemSelectedListener { menuitem ->
            when(menuitem.itemId){
                R.id.menuitem_logout -> activityViewModel.logout()
                R.id.menuitem_back -> requireActivity()?.findNavController(R.id.loggedin_navhost)?.
                    navigate(R.id.action_mainFragment2_to_routineFragment)
            }

             true}
}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoggedinBinding.inflate(inflater,container,false)
        activityViewModel.verifyCurrentUser()
        return binding.root
        //return inflater.inflate(R.layout.fragment_loggedin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottombuttonsnav(view)
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