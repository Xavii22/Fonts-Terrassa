package cat.copernic.projecte.fonts_terrassa

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentRegisterUserBinding
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.FirebaseFirestore

class RegisterUserFragment : Fragment() {

    private lateinit var binding: FragmentRegisterUserBinding
    private val db = FirebaseFirestore.getInstance()
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificacioId = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_register_user,
            container, false
        )
        super.onCreate(savedInstanceState)
        setup()

        binding.btnBack.setOnClickListener {
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
                            creacioCanalNotificacio()
                            db.collection("users").document(binding.emailEditText.text.toString())
                                .set(
                                    hashMapOf(
                                        "email" to binding.emailEditText.text.toString(),
                                        "active" to true
                                    )
                                )
                            enviarNotificacio()
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

    private fun showAlertExist() {
        val objectAlerDialog = android.app.AlertDialog.Builder(context)
        objectAlerDialog.setTitle(R.string.error)
        objectAlerDialog.setMessage(R.string.correu_introduit)
        objectAlerDialog.setPositiveButton(R.string.acceptar, null)
        var alertDialog: android.app.AlertDialog = objectAlerDialog.create()
        alertDialog.show()
    }

    private fun showAlertDifPass() {
        val objectAlerDialog = android.app.AlertDialog.Builder(context)
        objectAlerDialog.setTitle(R.string.error)
        objectAlerDialog.setMessage(R.string.contrasenya_no_igual)
        objectAlerDialog.setPositiveButton(R.string.acceptar, null)
        var alertDialog: android.app.AlertDialog = objectAlerDialog.create()
        alertDialog.show()
    }

    private fun showHome() {
        findNavController().navigate(RegisterUserFragmentDirections.actionRegisterUserFragmentToAdminFragment())
    }

    private fun creacioCanalNotificacio() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nom = "Titol de la notificació"
            val descripcio = "Descripció notificació."
            val importancia = NotificationManager.IMPORTANCE_DEFAULT
            val canal = NotificationChannel(CHANNEL_ID, nom, importancia)
            canal.description = descripcio

            val notificationManager: NotificationManager =
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(canal)
        }
    }

    private fun enviarNotificacio() {
        val resultIntent: Intent = Intent(context, RegisterUserFragment::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val resultPendingIntent = PendingIntent.getActivity(
            context, 0, resultIntent, 0
        )

        val mBuilder = context?.let {
            NotificationCompat.Builder(it, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_fonts_2)
                .setContentTitle("Fonts Terrassa")
                .setContentText("Administrador creat correctament.")
                .setContentIntent(resultPendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }

        if (mBuilder != null) {
            context?.let { NotificationManagerCompat.from(it) }?.notify(notificacioId, mBuilder.build())
        }
    }
}
