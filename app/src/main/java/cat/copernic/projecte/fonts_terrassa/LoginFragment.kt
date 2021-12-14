package cat.copernic.projecte.fonts_terrassa

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Login2Fragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val db = FirebaseFirestore.getInstance()

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
            if (binding.emailEditText.text.toString()
                    .isNotEmpty() && binding.passwordEditText.text.toString().isNotEmpty()
            ) {
                auth.signInWithEmailAndPassword(
                    binding.emailEditText.text.toString(), binding.passwordEditText.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        crearCorrutina()
                    } else {
                        showAlertIncorrect()
                    }
                }
            }
        }
    }

    private fun showAlertInactive() {
        val objectAlerDialog = AlertDialog.Builder(context)
        objectAlerDialog.setTitle("ERROR")
        objectAlerDialog.setMessage("Usuari inactiu")
        objectAlerDialog.setPositiveButton("Acceptar", null)
        var alertDialog: AlertDialog = objectAlerDialog.create()
        alertDialog.show()
    }

    private fun showAlertIncorrect() {
        val objectAlerDialog = AlertDialog.Builder(context)
        objectAlerDialog.setTitle("ERROR")
        objectAlerDialog.setMessage("Les dades introduides son incorrectes")
        objectAlerDialog.setPositiveButton("Acceptar", null)
        var alertDialog: AlertDialog = objectAlerDialog.create()
        alertDialog.show()
    }

    private fun showHome() {
        findNavController().navigate(Login2FragmentDirections.actionLoginFragmentToAdminFragment())
    }

    private fun crearCorrutina() =
        GlobalScope.launch(
            Dispatchers.Main
        ) {
            val auth: FirebaseAuth = FirebaseAuth.getInstance()
            db.collection("users")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.get("email")
                                .toString() == binding.emailEditText.text.toString()
                        ) {
                            if (document.get("active").toString().toBoolean()) {
                                auth.signOut()
                                showHome()
                            } else {
                                showAlertInactive()
                            }
                        }
                    }
                }

        }

    suspend fun suspensio(duracio: Long): Boolean {
        delay(duracio)
        return true
    }

}
