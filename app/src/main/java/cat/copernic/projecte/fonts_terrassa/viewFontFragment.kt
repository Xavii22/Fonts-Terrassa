package cat.copernic.projecte.fonts_terrassa

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentViewFontBinding
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_view_font.*


class viewFontFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var storageRef: StorageReference
    private lateinit var binding: viewFontFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentViewFontBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_view_font, container, false
        )

        binding.btnTestWater.setOnClickListener {
            findNavController().navigate(viewFontFragmentDirections.actionViewFontFragmentToEvaluateFragment())
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(viewFontFragmentDirections.actionViewFontFragmentToFragmentList())
        }

        val fontName = arguments?.getString("font_name")

        db.collection("fonts")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if (fontName == document.get("name").toString()) {
                        binding.txtNomFont.text = document.get("name").toString()
                        binding.txtInformacio.text = document.get("info").toString()

                        binding.btnGoToMaps.setOnClickListener {
                            val latitude = document.get("lat").toString().toDouble()
                            val longitude = document.get("lon").toString().toDouble()
                            val label = document.get("name").toString()
                            val uriBegin = "geo:$latitude,$longitude"
                            val query = "$latitude,$longitude($label)"
                            val encodedQuery = Uri.encode(query)
                            val uriString = "$uriBegin?q=$encodedQuery&z=14"
                            val uri = Uri.parse(uriString)
                            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            startActivity(mapIntent)
                        }
                    }
                }
            }
        /*
            binding.btnGoToMaps.setOnClickListener {
                val gmmIntentUri: Uri = Uri.parse(fontPos)
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        }*/

        if (fontName != null) {
            descarregarImatgeGlide(fontName)
        }

        return binding.root
    }

    private fun descarregarImatgeGlide(fontId: String) {
        val imgPath = "images/" + fontId + ".jpg"
        val imageRef = storageRef.child(imgPath)
        imageRef.downloadUrl.addOnSuccessListener { url ->

            Glide.with(this)
                .load(url.toString())
                .centerInside()
                .placeholder((R.drawable.loading))
                .error(R.drawable.ic_noimage)
                .into(binding.img_font)

        }.addOnFailureListener {
            binding.img_font.setImageResource(R.drawable.ic_noimage)
        }
    }
}
