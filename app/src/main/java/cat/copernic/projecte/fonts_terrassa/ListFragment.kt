package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentListBinding

class ListFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list, container, false)

        binding.cardView.setOnClickListener{
            findNavController().navigate(ListFragmentDirections.actionFragmentListToViewFontFragment())
        }

        return binding.root
    }
}