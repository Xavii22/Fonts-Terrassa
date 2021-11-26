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
import android.util.Log
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentInfoBinding
import com.google.firebase.firestore.FirebaseFirestore

class RegisterUserFragment : Fragment() {

    private lateinit var binding: FragmentRegisterUserBinding
    private val db= FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,
            cat.copernic.projecte.fonts_terrassa.R.layout.fragment_register_user,
            container,false)
        super.onCreate(savedInstanceState)
        setup()

        binding.btnBack.setOnClickListener{
            findNavController().navigate(RegisterUserFragmentDirections.actionRegisterUserFragmentToAdminFragment())
        }

        return binding.root
    }

    private fun setup() {

        binding.registerButton.setOnClickListener {

            if (binding.emailEditText.text.toString()
                    .isNotEmpty() && binding.passwordEditText.text.toString().isNotEmpty()
                && binding.repeatEditText.text.toString().isNotEmpty()
            ) {
                if (binding.passwordEditText.text.toString() == binding.repeatEditText.text.toString()) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.emailEditText.text.toString(),
                        binding.passwordEditText.text.toString()
                    ).addOnCompleteListener {

                        if (it.isSuccessful) {
                            //Save email to database
                            db.collection("users").document(binding.emailEditText.text.toString())
                                .set(
                                    hashMapOf(
                                        "email" to binding.emailEditText.text.toString(),
                                        "active" to true
                                    )
                                )
                            //Go to Dashboard
                            showHome()
                        } else {
                            showAlertExist()
                        }
                    }
                } else {
                    showAlertDifPass()
                }
            }
        }
    }

    private fun showAlertExist(){
        val objectAlerDialog = android.app.AlertDialog.Builder(context)
        objectAlerDialog.setTitle("ERROR")
        objectAlerDialog.setMessage("El correu introduit ja existeix a la base de dades")
        objectAlerDialog.setPositiveButton("Acceptar",null)
        var alertDialog: android.app.AlertDialog = objectAlerDialog.create()
        alertDialog.show()
    }

    private fun showAlertDifPass(){
        val objectAlerDialog = android.app.AlertDialog.Builder(context)
        objectAlerDialog.setTitle("ERROR")
        objectAlerDialog.setMessage("Les contrasenyes introduides no son iguals")
        objectAlerDialog.setPositiveButton("Acceptar",null)
        var alertDialog: android.app.AlertDialog = objectAlerDialog.create()
        alertDialog.show()
    }

    private fun showHome() {
        findNavController().navigate(RegisterUserFragmentDirections.actionRegisterUserFragmentToAdminFragment())
    }
}
