package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentRegisterUserBinding
import com.google.firebase.auth.FirebaseAuth

import android.R
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentInfoBinding

class RegisterUserFragment : Fragment() {

    private lateinit var binding: FragmentRegisterUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,
            cat.copernic.projecte.fonts_terrassa.R.layout.fragment_register_user,
            container,false)
        super.onCreate(savedInstanceState)
        setup()

        return binding.root
    }

    private fun setup() {

        binding.registerButton.setOnClickListener {

            if(binding.emailEditText.text.toString().isNotEmpty() && binding.passwordEditText.text.toString().isNotEmpty()
                && binding.repeatEditText.text.toString().isNotEmpty()) {
                if (binding.passwordEditText.text.toString() == binding.repeatEditText.text.toString()) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.emailEditText.text.toString(),
                        binding.passwordEditText.text.toString()
                    ).addOnCompleteListener {

                        if (it.isSuccessful) {
                            showHome()
                        }
                    }
                } else {
                    contrasenyaRepetida()
                }
            }
        }
    }

    private fun showHome() {
        findNavController().navigate(RegisterUserFragmentDirections.actionRegisterUserFragmentToAdminFragment())
    }

    private fun contrasenyaRepetida() {
        val builder1 = AlertDialog.Builder(
            requireContext()
        )
        builder1.setMessage("Contrasenya repetida")
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            "Yes"
        ) { dialog, id -> dialog.cancel() }

        builder1.setNegativeButton(
            "No"
        ) { dialog, id -> dialog.cancel() }

        val alert11 = builder1.create()
        alert11.show()

    }
}