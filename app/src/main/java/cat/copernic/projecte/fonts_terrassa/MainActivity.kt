package cat.copernic.projecte.fonts_terrassa

import android.Manifest
import android.app.Activity
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import cat.copernic.projecte.fonts_terrassa.databinding.ActivityMainBinding
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        loadLocale()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.navView, navController)

        requestLocationPermissions()

        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
            return
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if ((ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) ==
                                PackageManager.PERMISSION_GRANTED)
                    ) {
                        finish()
                        startActivity(intent)
                    }
                }
                return
            }
        }
    }

    fun setLocale(lang:String){
        if(lang != getLocale()){
            val locale: Locale = Locale(lang)
            Locale.setDefault(locale)
            val config: Configuration = Configuration()
            config.locale = locale
            baseContext.resources.updateConfiguration(config,baseContext.resources.displayMetrics)

            val editor: SharedPreferences.Editor = getSharedPreferences ("Settings", MODE_PRIVATE).edit()
            editor.putString("My_Lang", lang)
            editor.apply()
            reloadActivity()
        }
    }

    fun loadLocale(){
        val prefs: SharedPreferences = getSharedPreferences ("Settings", Activity.MODE_PRIVATE)
        val language: String = prefs.getString("My_Lang", "").toString()
        setLocale(language)
    }

    fun getLocale() : String{
        val prefs: SharedPreferences = getSharedPreferences ("Settings", Activity.MODE_PRIVATE)
        return prefs.getString("My_Lang", "").toString()
    }

    fun reloadActivity(){
        finish()
        startActivity(getIntent())
    }
}