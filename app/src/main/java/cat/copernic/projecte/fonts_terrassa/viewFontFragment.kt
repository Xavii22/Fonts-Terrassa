
package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentViewFontBinding

class viewFontFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentViewFontBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_view_font, container, false)

        binding.btnTestWater.setOnClickListener{
            findNavController().navigate(viewFontFragmentDirections.actionViewFontFragmentToEvaluateFragment())
        }

        return binding.root
    }
}