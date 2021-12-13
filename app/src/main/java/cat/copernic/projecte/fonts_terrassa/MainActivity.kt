package cat.copernic.projecte.fonts_terrassa

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import cat.copernic.projecte.fonts_terrassa.databinding.ActivityMainBinding
import com.google.firebase.analytics.FirebaseAnalytics

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

        requestLocationPermissions()
    }

    private fun requestLocationPermissions(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED)) {
                        finish()
                        startActivity(intent)
                    }
                }
                return
            }
        }
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