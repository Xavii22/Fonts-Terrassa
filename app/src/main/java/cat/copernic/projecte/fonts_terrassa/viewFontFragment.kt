
package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentViewFontBinding
import android.content.Intent
import android.net.Uri


class viewFontFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentViewFontBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_view_font, container, false)

        binding.btnTestWater.setOnClickListener{
            findNavController().navigate(viewFontFragmentDirections.actionViewFontFragmentToEvaluateFragment())
        }

        binding.btnGoToMaps.setOnClickListener{
            val gmmIntentUri: Uri = Uri.parse("geo:0,0?q=41.5805282,2.022369")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        return binding.root
    }
}