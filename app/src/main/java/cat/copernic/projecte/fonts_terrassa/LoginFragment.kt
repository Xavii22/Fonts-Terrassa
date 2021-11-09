package cat.copernic.projecte.fonts_terrassa

import android.content.Intent
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
        binding.loginButton.setOnClickListener{
            if(binding.emailEditText.text.isNotEmpty()&& binding.passwordEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.emailEditText.toString()
                    ,binding.passwordEditText.toString()).addOnCompleteListener{
                    if(it.isSuccessful) {
                        Log.d("Prova completada", binding.emailEditText.text.toString())
                        Log.d("Prova completada", binding.passwordEditText.text.toString())
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