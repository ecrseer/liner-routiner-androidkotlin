package br.infnet.dk_tp1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.ui.setupWithNavController
import br.infnet.dk_tp1.R
import br.infnet.dk_tp1.databinding.FragmentLoggedinBinding
import br.infnet.dk_tp1.ui.main.MainFragment
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentLoggedinBinding.inflate(inflater,container,false)
        return binding.root

    }
    private fun setupBottomNavigation(view:View){

        val bottomBtns: BottomNavigationView = binding.bottomNavBtns
        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.loggedmain_navhost) as NavHostFragment
        val navController = findNavController(navHostFragment)
        bottomBtns.setupWithNavController(navController)

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