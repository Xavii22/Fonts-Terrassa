package cat.copernic.projecte.fonts_terrassa

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentViewFontBinding
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_view_font.*


class viewFontFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference
    private lateinit var binding: FragmentViewFontBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_view_font, container, false
        )

        binding.btnTestWater.setOnClickListener {
            findNavController().navigate(viewFontFragmentDirections.actionViewFontFragmentToEvaluateFragment())
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(viewFontFragmentDirections.actionViewFontFragmentToFragmentList())
        }

        val fontName = arguments?.getString("font_name")
        when (arguments?.getInt("tipus_font")) {
            1 -> binding.tipusFontTxt.text =
                (context?.getString(R.string.fonts_boca) ?: 1) as CharSequence?
            2 -> binding.tipusFontTxt.text =
                (context?.getString(R.string.fonts_boca_singulars) ?: 2) as CharSequence?
            3 -> binding.tipusFontTxt.text =
                (context?.getString(R.string.fonts_ornamentals) ?: 3) as CharSequence?
            4 -> binding.tipusFontTxt.text =
                (context?.getString(R.string.fonts_naturals) ?: 4) as CharSequence?
            5 -> binding.tipusFontTxt.text =
                (context?.getString(R.string.fonts_gossos) ?: 5) as CharSequence?

        }


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
                .into(binding.imgFont)

        }.addOnFailureListener {
            binding.imgFont.setImageResource(R.drawable.ic_noimage)
        }
    }
}
