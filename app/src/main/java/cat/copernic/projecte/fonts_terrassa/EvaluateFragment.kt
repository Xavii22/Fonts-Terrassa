
package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentEvaluateBinding

class EvaluateFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentEvaluateBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_evaluate, container, false)

        binding.imageButton2.setOnClickListener{
            findNavController().navigate(EvaluateFragmentDirections.actionEvaluateFragmentToViewFontFragment())
        }

        return binding.root
    }
}