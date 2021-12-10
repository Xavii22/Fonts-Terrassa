package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
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
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.navView, navController)

        val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "hola firebase")
        analytics.logEvent("Initscreen", bundle)
        /*
        //var tasca1: Job? = null

        /*tasca1 = crearCorrutina(
            10
        )*/

        tasca1.let {
            tasca1?.cancel().apply { //Si cancelem l'execució de la tasca 1...
                //Creem un Toast perquè en mostri un missatge durant un temps
                Toast.makeText(
                    this@MainActivity, //Context del Toast
                    "Corrutina 1 cancelada!!", //Missatge que mostrarà el Toast
                    Toast.LENGTH_SHORT //Temps curt
                ).show()
            }
        }

        //var tasca2: Job? = null

        /*tasca2 = crearCorrutina(
            20
        )*/

        tasca2.let {
            tasca2?.cancel().apply { //Si cancelem l'execució de la tasca 2...
                Toast.makeText(
                    this@MainActivity,
                    "Corrutina 2 cancelada!!",
                    Toast.LENGTH_LONG //Temps llarg
                ).show()
            }
        }
*/
    }
    /*
    private fun crearCorrutina(durada: Int) = GlobalScope.launch(
        Dispatchers.Main
    ) {

        withContext(Dispatchers.IO) {
            var comptador = 0
            while (comptador < durada) {
                if (suspensio((durada * 50).toLong())) {
                    comptador++
                }
            }
        }

        Toast.makeText(
            this@MainActivity,
            "Finalitzada!!",
            Toast.LENGTH_SHORT
        ).show()
    }

    suspend fun suspensio(duracio: Long): Boolean {
        delay(duracio)
        return true
    }*/
}