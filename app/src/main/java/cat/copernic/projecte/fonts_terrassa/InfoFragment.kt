
package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentInfoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_map, container, false)

        binding.btnBeure.setOnClickListener {
            findNavController().navigate(InfoFragmentDirections.actionFragmentInfoToInfoGosFragment())
        }
        binding.btnBeureSingular.setOnClickListener {
            findNavController().navigate(InfoFragmentDirections.actionFragmentInfoToInfoGosFragment())
        }
        binding.btnOrnamental.setOnClickListener {
            findNavController().navigate(InfoFragmentDirections.actionFragmentInfoToInfoGosFragment())
        }
        binding.btnNatural.setOnClickListener {
            findNavController().navigate(InfoFragmentDirections.actionFragmentInfoToInfoGosFragment())
        }

        binding.btnGos.setOnClickListener {
            findNavController().navigate(InfoFragmentDirections.actionFragmentInfoToInfoGosFragment())
        }
        return binding.root
    }
}
