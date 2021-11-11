package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_login.*

class Login2Fragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false)
        super.onCreate(savedInstanceState)
        setup()

        return binding.root
    }



    private fun setup(){
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener{
            if(binding.emailEditText.text.isNotEmpty()&& binding.passwordEditText.text.isNotEmpty()){
                auth.signInWithEmailAndPassword(binding.emailEditText.text.toString()
                    ,binding.passwordEditText.text.toString()
                ).addOnCompleteListener{
                    Log.d("prova", it.exception?.printStackTrace().toString())
                    if(it.isSuccessful) {
                        showHome()
                    }
                }
            }
        }
    }

    private fun showHome(){
        findNavController().navigate(Login2FragmentDirections.actionLoginFragmentToAdminFragment())
    }
}