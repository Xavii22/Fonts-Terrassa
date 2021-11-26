package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )
        super.onCreate(savedInstanceState)
        setup()

        binding.textviewContrasenya.setOnClickListener {
            findNavController().navigate(Login2FragmentDirections.actionLoginFragmentToRecuperarFragment())
        }

        return binding.root
    }

    private fun setup() {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            if(binding.emailEditText.text.toString().isNotEmpty() && binding.passwordEditText.text.toString().isNotEmpty()){
                auth.signInWithEmailAndPassword(
                    binding.emailEditText.text.toString(), binding.passwordEditText.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        auth.signOut()
                        showHome()
                    }
                }
            }
        }
    }

    private fun showHome() {
        findNavController().navigate(Login2FragmentDirections.actionLoginFragmentToAdminFragment())
    }

}
