package br.infnet.dk_tp1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.infnet.dk_tp1.databinding.MainActivityBinding
import br.infnet.dk_tp1.ui.LoggedinFragment
import br.infnet.dk_tp1.ui.MainActivityViewModel
import br.infnet.dk_tp1.ui.login.LoginFragment
import br.infnet.dk_tp1.ui.main.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding

    val activityViewModel: MainActivityViewModel by viewModels()
    fun goToPage(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitNow()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {

            goToPage(LoginFragment.newInstance(""))
        }
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activityViewModel.isUserLoggedIn.observe(this@MainActivity, Observer { loggedIn ->
            loggedIn?.let {
                if (loggedIn) goToPage(LoggedinFragment.newInstance(""))
                else goToPage(LoginFragment.newInstance(""))
            }
        })


    }
}