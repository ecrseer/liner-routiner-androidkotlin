package br.infnet.dk_tp1.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import br.infnet.dk_tp1.R
import br.infnet.dk_tp1.databinding.FragmentLoginBinding
import br.infnet.dk_tp1.ui.MainActivityViewModel
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private val ARG_PARAM1 = "param1"
    // TODO: Rename and change types of parameters
    private var param1: String? = null


    private lateinit var binding: FragmentLoginBinding
    private lateinit var facebookCallbackMan:CallbackManager

    val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }
    private fun handleFacebookAccessToken(token: AccessToken) {

        val credential = FacebookAuthProvider.getCredential(token.token)
        with(activityViewModel){

        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    verifyCurrentUser()
                    mUserLiveData.postValue(mAuth.currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    println("signInWithCredential:failure${task.exception}")
                    Toast.makeText(requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
    private fun setupFacb(){

        with(binding.facebookLoginButton) {
            setPermissions("email", "public_profile")
            registerCallback(facebookCallbackMan,
                object : FacebookCallback<LoginResult> {

                    override fun onSuccess(loginResult: LoginResult) {
                        handleFacebookAccessToken(loginResult.accessToken)
                    }

                    override fun onCancel() {
                        //Log.d(TAG, "facebook:onCancel")
                    }

                    override fun onError(error: FacebookException) {

                    }
                })

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        facebookCallbackMan = CallbackManager.Factory.create()

        //binding.facebookLoginButton.setReadPermissions("email","public_profile")



        return binding.root
        //return inflater.inflate(R.layout.fragment_login, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainSigninBtn.setOnClickListener {
            SignInDialog().show(childFragmentManager, "Entrar")
        }
        binding.mainLoginBtn.setOnClickListener {
            SignUpDialog().show(childFragmentManager, "Criar")
        }
        setupFacb()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}