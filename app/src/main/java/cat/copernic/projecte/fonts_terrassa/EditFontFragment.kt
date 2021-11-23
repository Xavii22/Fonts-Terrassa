package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentEditFontBinding
import com.google.firebase.firestore.FirebaseFirestore

class EditFontFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private var oldFontName = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentEditFontBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_font, container, false)

        binding.txtNomFont.setText(arguments?.getString("font_name"))
        binding.editLat.setText(arguments?.getString("font_lat"))
        binding.editLon.setText(arguments?.getString("font_lon"))

        binding.btnSave.setOnClickListener{
            if(binding.txtNomFont.text.isNotEmpty() and binding.editLat.text.isNotEmpty() and binding.editLon.text.isNotEmpty()){

                db.collection("fonts").document(oldFontName)
                    .delete()

                db.collection("fonts").document(binding.txtNomFont.text.toString())
                    .set(hashMapOf(
                        "lat" to binding.editLat.text.toString().toDouble(),
                        "lon" to binding.editLon.text.toString().toDouble(),
                        "name" to binding.txtNomFont.text.toString(),
                        "type" to 1
                    ))
                findNavController().navigate(EditFontFragmentDirections.actionEditFontFragmentToAdminFragment())
            }
        }

        return binding.root
    }
}
