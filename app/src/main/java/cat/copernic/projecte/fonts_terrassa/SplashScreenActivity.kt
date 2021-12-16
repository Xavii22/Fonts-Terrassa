package cat.copernic.projecte.fonts_terrassa

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.util.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        loadLocale()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        img_loading.alpha = 0f
        img_loading.animate().setDuration(1000).alpha(1f).withEndAction{
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    fun setLocale(lang: String, value: Int) {
            val locale: Locale = Locale(lang)
            Locale.setDefault(locale)
            val config: Configuration = Configuration()
            config.locale = locale
            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

            val editor: SharedPreferences.Editor = getSharedPreferences("Settings", MODE_PRIVATE).edit()
            editor.putString("My_Lang", lang)
            editor.apply()
    }

    fun loadLocale() {
        val prefs: SharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language: String = prefs.getString("My_Lang", "").toString()
        setLocale(language, 0)
    }
}