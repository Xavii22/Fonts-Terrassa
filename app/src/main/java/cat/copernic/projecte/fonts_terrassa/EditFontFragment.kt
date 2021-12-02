package cat.copernic.projecte.fonts_terrassa

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentEditFontBinding
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class EditFontFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private var oldFontName = ""
    private var hasImage = false
    private lateinit var imageView: ImageView
    private lateinit var storageRef: StorageReference
    private lateinit var binding: FragmentEditFontBinding
    private var latestTmpUri: Uri? = null
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    binding.imgFont.setImageURI(uri)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_font, container, false)

        binding.txtNomFont.setText(arguments?.getString("font_name"))
        oldFontName = binding.txtNomFont.text.toString()
        binding.editLat.setText(arguments?.getString("font_lat"))
        binding.editLon.setText(arguments?.getString("font_lon"))
        binding.txtInformacio.setText(arguments?.getString("font_info"))
        (arguments?.getInt("font_type"))?.minus(1)?.let { binding.spinnerType.setSelection(it) }

        imageView = binding.imgFont
        storageRef = FirebaseStorage.getInstance().reference

        binding.btnUpload.setOnClickListener {
            openGallery()
        }

        binding.btnBack.setOnClickListener {
            if (loseChanges()) {
                findNavController().navigate(EditFontFragmentDirections.actionEditFontFragmentToAdminFragment())
            }
        }

        binding.btnRemoveImg.setOnClickListener {
            //Delete Image
            if (hasImage) {
                deleteImage(binding.txtNomFont.text.toString())
                hasImage = false
            }
        }

        binding.btnSave.setOnClickListener {
            //Save font data
            if (binding.txtNomFont.text.isNotEmpty() and binding.editLat.text.isNotEmpty() and binding.editLon.text.isNotEmpty()) {
                //Delete old image
                deleteImage(oldFontName)
                if (hasImage) {
                    //Upload image
                    pujarImatge(binding.txtNomFont.text.toString())
                }

                if (binding.txtNomFont.text.toString() != oldFontName) {
                    db.collection("fonts")
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                if (document.get("name") == oldFontName) {
                                    db.collection("fonts").document(oldFontName).delete()
                                }
                            }
                        }
                }

                db.collection("fonts").document(binding.txtNomFont.text.toString())
                    .set(
                        hashMapOf(
                            "lat" to binding.editLat.text.toString().toDouble(),
                            "lon" to binding.editLon.text.toString().toDouble(),
                            "name" to binding.txtNomFont.text.toString(),
                            "info" to binding.txtInformacio.text.toString(),
                            "type" to binding.spinnerType.selectedItemPosition + 1
                        )
                    )
                findNavController().navigate(EditFontFragmentDirections.actionEditFontFragmentToAdminFragment())
            }
        }

        descarregarImatgeGlide(oldFontName)

        return binding.root
    }

    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.data
            binding?.imgFont?.setImageURI(data)
            hasImage = true
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }

    private fun pujarImatge(fontId: String) {
        val imgPath = "images/" + fontId + ".jpg"
        val pathReference = storageRef.child(imgPath)
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = pathReference.putBytes(data)
        uploadTask.addOnFailureListener {
            //Error al pujar
        }.addOnSuccessListener {
            //Pujat correctament
        }
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
                .into(imageView)
            hasImage = true

        }.addOnFailureListener {
            binding.imgFont.setImageResource(R.drawable.ic_noimage)
            hasImage = false
        }
    }

    private fun deleteImage(fontId: String) {
        val imgPath = "images/" + fontId + ".jpg"
        val imageRef = storageRef.child(imgPath)
        imageRef.delete().addOnSuccessListener {
            // File deleted successfully
            binding.imgFont.setImageResource(R.drawable.ic_noimage)
        }.addOnFailureListener {
            // Filed to remove the image
        }
    }

    private fun loseChanges(): Boolean {
        var isAccepted = false
        val objectAlerDialog = AlertDialog.Builder(context)
        objectAlerDialog.setTitle("Advertencia")
        objectAlerDialog.setMessage("Si surts es perdràn els canvis...")
        objectAlerDialog.setPositiveButton("Acceptar i sortir") { dialog, which ->
            isAccepted = true
        }
        objectAlerDialog.setNegativeButton("Seguir editant", null)
        var alertDialog: AlertDialog = objectAlerDialog.create()
        alertDialog.show()

        return isAccepted
    }
}
