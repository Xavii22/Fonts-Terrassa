package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import cat.copernic.projecte.fonts_terrassa.databinding.ActivityMainBinding
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.navView, navController)

        val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "hola firebase")
        analytics.logEvent("Initscreen", bundle)


    }
/*
    private fun crearCorrutina(durada: Int, inici: Button, cancelar: Button, progres: ProgressBar) = GlobalScope.launch(
        Dispatchers.Main) {
        inici.isEnabled = false
        cancelar.isEnabled = true
        progres.progress = 0

        withContext(Dispatchers.IO) {
            var comptador = 0
            while (comptador < durada) {
                if(suspensio((durada * 50).toLong())) {
                    comptador++
                    progres.progress = (comptador * 100) / durada
                }
            }
        }

        inici.isEnabled = true
        cancelar.isEnabled = false
        progres.progress = 0
        Toast.makeText(
            this@MainActivity,
            "${inici.text} Finalitzada!!",
            Toast.LENGTH_SHORT
        ).show()
    }

    suspend fun suspensio(duracio: Long): Boolean {
        delay(duracio)
        return true
    }*/
}