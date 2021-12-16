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
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class EditFontFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private var oldFontId = ""
    private var hasImage = false
    private lateinit var imageView: ImageView
    private lateinit var storageRef: StorageReference
    private lateinit var binding: FragmentEditFontBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_font, container, false)

        binding.inputIdFont.setText(arguments?.getString("id_font"))
        binding.txtNomFont.setText(arguments?.getString("font_name"))
        oldFontId = binding.inputIdFont.text.toString()
        binding.editLat.setText(arguments?.getString("font_lat"))
        binding.editLon.setText(arguments?.getString("font_lon"))
        binding.txtInformacio.setText(arguments?.getString("font_info"))
        binding.editAdreca.setText(arguments?.getString("font_adreca"))
        (arguments?.getInt("font_type"))?.minus(1)?.let { binding.spinnerType.setSelection(it) }

        imageView = binding.imgFont
        storageRef = FirebaseStorage.getInstance().reference

        binding.btnUpload.setOnClickListener {
            openGallery()
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(EditFontFragmentDirections.actionEditFontFragmentToFontAdminListFragment())
        }

        binding.btnRemoveImg.setOnClickListener {
            //Delete Image
            if (hasImage) {
                deleteImage(binding.inputIdFont.text.toString())
                hasImage = false
            }
            Snackbar.make(
                binding.frameLayout,
                R.string.imatge_eliminada,
                BaseTransientBottomBar.LENGTH_SHORT
            ).show()
        }

        binding.btnSave.setOnClickListener {
            //Save font data
            if (binding.inputIdFont.text.isNotEmpty() and binding.txtNomFont.text.isNotEmpty() and binding.editLat.text.isNotEmpty() and binding.editLon.text.isNotEmpty()) {
                db.collection("fonts")
                    .get()
                    .addOnSuccessListener { documents ->
                        var fontIdFound = false
                        val myId = binding.inputIdFont.text.toString()
                        for (document in documents) {
                            if (document.get("id") == myId && oldFontId == "") {
                                fontIdFound = true
                            }
                        }
                        if (!fontIdFound) {
                            //Delete old image
                            deleteImageBeforeUploading(oldFontId)
                            if (hasImage) {
                                //Upload image
                                pujarImatge(binding.inputIdFont.text.toString())
                            }

                            if (binding.inputIdFont.text.toString() != oldFontId) {
                                db.collection("fonts")
                                    .get()
                                    .addOnSuccessListener { documents ->
                                        for (document in documents) {
                                            if (document.get("id") == oldFontId) {
                                                db.collection("fonts").document(oldFontId)
                                                    .delete()
                                            }
                                        }
                                    }
                            }

                            db.collection("fonts").document(binding.inputIdFont.text.toString())
                                .set(
                                    hashMapOf(
                                        "lat" to binding.editLat.text.toString().toDouble(),
                                        "lon" to binding.editLon.text.toString().toDouble(),
                                        "id" to binding.inputIdFont.text.toString(),
                                        "name" to binding.txtNomFont.text.toString(),
                                        "info" to binding.txtInformacio.text.toString(),
                                        "address" to binding.editAdreca.text.toString(),
                                        "type" to binding.spinnerType.selectedItemPosition + 1
                                    )
                                )
                            findNavController().navigate(EditFontFragmentDirections.actionEditFontFragmentToFontAdminListFragment())
                        } else {
                            showAlertIdExist()
                        }
                    }
            } else {
                showBlankFields()
            }
        }

        descarregarImatgeGlide(oldFontId)

        return binding.root
    }

    private fun showAlertIdExist() {
        val objectAlerDialog = AlertDialog.Builder(context)
        objectAlerDialog.setTitle(R.string.error)
        objectAlerDialog.setMessage(R.string.id_font_existeix)
        objectAlerDialog.setPositiveButton(R.string.acceptar, null)
        val alertDialog: AlertDialog = objectAlerDialog.create()
        alertDialog.show()
    }

    private fun showBlankFields() {
        val objectAlerDialog = AlertDialog.Builder(context)
        objectAlerDialog.setTitle(R.string.error)
        objectAlerDialog.setMessage(R.string.camps_no_omplerts)
        objectAlerDialog.setPositiveButton(R.string.acceptar, null)
        val alertDialog: AlertDialog = objectAlerDialog.create()
        alertDialog.show()
    }

    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.data
            binding.imgFont.setImageURI(data)
            hasImage = true
            Snackbar.make(
                binding.frameLayout,
                R.string.imatge_pujada_correctament,
                BaseTransientBottomBar.LENGTH_SHORT
            ).show()
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
        }.addOnFailureListener {
            // Filed to remove the image
        }
        binding.imgFont.setImageResource(R.drawable.ic_noimage)
    }

    private fun deleteImageBeforeUploading(fontId: String) {
        val imgPath = "images/" + fontId + ".jpg"
        val imageRef = storageRef.child(imgPath)
        imageRef.delete().addOnSuccessListener {
            // File deleted successfully
        }.addOnFailureListener {
            // Filed to remove the image
        }
    }
}
