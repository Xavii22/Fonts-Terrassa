package cat.copernic.projecte.fonts_terrassa

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentRecuperarBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecuperarFragment : Fragment() {
    private lateinit var binding: FragmentRecuperarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_recuperar, container, false
        )
        super.onCreate(savedInstanceState)
        setup()

        return binding.root
    }

    private fun setup() {
        binding.retrieveButton.setOnClickListener {
            val emailAddress = binding.emailEditText.text.toString()
            if (binding.emailEditText.text.toString().isNotEmpty()) {
                Firebase.auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Email sent.")
                        } else {
                            Log.d(TAG, "Email NOT sent.")
                        }
                    }
            }
        }
    }
}