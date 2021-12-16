package cat.copernic.projecte.fonts_terrassa

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentInfoBinding
import java.util.*

class InfoFragment : Fragment() {

    var switchCompat: Switch? = null
    var sharedPreferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentInfoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_info, container, false
        )

        when((activity as MainActivity?)?.getLocale()){
            "ca" ->
                binding.spinnerLanguage.setSelection(0)
            "es" ->
                binding.spinnerLanguage.setSelection(1)
            "en" ->
                binding.spinnerLanguage.setSelection(2)
        }

        switchCompat = binding.switch1
        sharedPreferences = activity?.getSharedPreferences("night", 0)
        val booleanValue = sharedPreferences?.getBoolean("night_mode", true)
        if (booleanValue == true) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            switchCompat!!.setChecked(true)
        }
        switchCompat!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchCompat!!.setChecked(true)
                val editor = sharedPreferences?.edit()
                if (editor != null) {
                    editor.putBoolean("night_mode", true)
                }
                if (editor != null) {
                    editor.commit()
                }
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchCompat!!.setChecked(false)
                val editor = sharedPreferences?.edit()
                if (editor != null) {
                    editor.putBoolean("night_mode", false)
                }
                if (editor != null) {
                    editor.commit()
                }
            }
        })

        changeLanguage(binding)

        binding.btnLoginAdmin.setOnClickListener {
            findNavController().navigate(InfoFragmentDirections.actionFragmentInfoToLoginFragment())
        }
        binding.btnBeure.setOnClickListener {
            findNavController().navigate(InfoFragmentDirections.actionFragmentInfoToInfoBeureFragment())
        }
        binding.btnSingular.setOnClickListener {
            findNavController().navigate(InfoFragmentDirections.actionFragmentInfoToInfoSingularFragment())
        }
        binding.btnOrnamental.setOnClickListener {
            findNavController().navigate(InfoFragmentDirections.actionFragmentInfoToInfoOrnamentalFragment())
        }
        binding.btnNatural.setOnClickListener {
            findNavController().navigate(InfoFragmentDirections.actionFragmentInfoToInfoNaturalFragment())
        }

        binding.btnGos.setOnClickListener {
            findNavController().navigate(InfoFragmentDirections.actionFragmentInfoToInfoGosFragment())
        }
        return binding.root

    }

    private fun changeLanguage(binding: FragmentInfoBinding) {

        binding.spinnerLanguage.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (binding.spinnerLanguage.selectedItem.toString()) {
                        "Català" -> {
                            (activity as MainActivity?)?.setLocale("ca", 1)
                        }
                        "Español" -> {
                            (activity as MainActivity?)?.setLocale("es", 1)
                        }
                        "English" -> {
                            (activity as MainActivity?)?.setLocale("en", 1)
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
    }

}