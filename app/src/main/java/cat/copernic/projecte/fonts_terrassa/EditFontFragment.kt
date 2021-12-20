package cat.copernic.projecte.fonts_terrassa

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
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
        savedInstanceState: Bundle?,
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_font, container, false)

        binding.inputIdFont.setText(arguments?.getString("id_font"))
        binding.txtNomFont.setText(arguments?.getString("font_name"))
        oldFontId = binding.inputIdFont.text.toString()
        binding.editLat.setText(arguments?.getString("font_lat"))
        binding.editLon.setText(arguments?.getString("font_lon"))
        binding.txtInformacio.setText(arguments?.getString("font_info"))
        binding.editAdreca.setText(arguments?.getString("font_adreca"))
        var myFontType = 0
        when (arguments?.getInt("font_type")) {
            1 -> {
                myFontType = 1
                binding.btnTypeSel1.setImageResource(R.drawable.gota_1_selected)
            }
            2 -> {
                myFontType = 2
                binding.btnTypeSel2.setImageResource(R.drawable.gota_2_selected)
            }
            3 -> {
                myFontType = 3
                binding.btnTypeSel3.setImageResource(R.drawable.gota_3_selected)
            }
            4 -> {
                myFontType = 4
                binding.btnTypeSel4.setImageResource(R.drawable.gota_4_selected)
            }
            5 -> {
                myFontType = 5
                binding.btnTypeSel5.setImageResource(R.drawable.gota_5_selected)
            }
        }

        imageView = binding.imgFont
        storageRef = FirebaseStorage.getInstance().reference

        binding.btnUpload?.setOnClickListener {
            openGallery()
        }

        binding.btnBack?.setOnClickListener {
            findNavController().navigate(EditFontFragmentDirections.actionEditFontFragmentToFontAdminListFragment())
        }

        binding?.btnRemoveImg?.setOnClickListener {
            //Delete Image
            if (hasImage) {
                deleteImage(binding?.inputIdFont?.text.toString())
                Log.d("msg", "arriba4")
                hasImage = false
                Snackbar.make(
                    binding.frameLayout,
                    R.string.imatge_eliminada,
                    BaseTransientBottomBar.LENGTH_SHORT
                ).show()
            }
            Log.d("msg", "arriba5")
        }


        binding.btnTypeSel1.setOnClickListener {
            myFontType = 1
            binding.btnTypeSel1.setImageResource(R.drawable.gota_1_selected)
            binding.btnTypeSel2.setImageResource(R.drawable.gota_2)
            binding.btnTypeSel3.setImageResource(R.drawable.gota_3)
            binding.btnTypeSel4.setImageResource(R.drawable.gota_4)
            binding.btnTypeSel5.setImageResource(R.drawable.gota_5)
        }
        binding.btnTypeSel2.setOnClickListener {
            myFontType = 2
            binding.btnTypeSel1.setImageResource(R.drawable.gota_1)
            binding.btnTypeSel2.setImageResource(R.drawable.gota_2_selected)
            binding.btnTypeSel3.setImageResource(R.drawable.gota_3)
            binding.btnTypeSel4.setImageResource(R.drawable.gota_4)
            binding.btnTypeSel5.setImageResource(R.drawable.gota_5)
        }
        binding.btnTypeSel3.setOnClickListener {
            myFontType = 3
            binding.btnTypeSel1.setImageResource(R.drawable.gota_1)
            binding.btnTypeSel2.setImageResource(R.drawable.gota_2)
            binding.btnTypeSel3.setImageResource(R.drawable.gota_3_selected)
            binding.btnTypeSel4.setImageResource(R.drawable.gota_4)
            binding.btnTypeSel5.setImageResource(R.drawable.gota_5)
        }
        binding.btnTypeSel4.setOnClickListener {
            myFontType = 4
            binding.btnTypeSel1.setImageResource(R.drawable.gota_1)
            binding.btnTypeSel2.setImageResource(R.drawable.gota_2)
            binding.btnTypeSel3.setImageResource(R.drawable.gota_3)
            binding.btnTypeSel4.setImageResource(R.drawable.gota_4_selected)
            binding.btnTypeSel5.setImageResource(R.drawable.gota_5)
        }
        binding.btnTypeSel5.setOnClickListener {
            myFontType = 5
            binding.btnTypeSel1.setImageResource(R.drawable.gota_1)
            binding.btnTypeSel2.setImageResource(R.drawable.gota_2)
            binding.btnTypeSel3.setImageResource(R.drawable.gota_3)
            binding.btnTypeSel4.setImageResource(R.drawable.gota_4)
            binding.btnTypeSel5.setImageResource(R.drawable.gota_5_selected)
        }

        binding.btnSave.setOnClickListener {
            //Save font data
            if (binding.txtNomFont.text.isNotEmpty() && binding.editLat.text.isNotEmpty() && binding.editLon.text.isNotEmpty() && myFontType != 0) {
                var calulatedStringId = oldFontId
                db.collection("fonts")
                    .get()
                    .addOnSuccessListener { documents ->
                        if (oldFontId == "") {
                            var calculatedId = 0
                            for (document in documents) {
                                val actualId =
                                    document.get("id").toString().substring(3).toInt()
                                if (actualId >= calculatedId) {
                                    calculatedId = actualId + 1
                                }
                            }

                            calulatedStringId = when (calculatedId.toString().length) {
                                1 ->
                                    "FBO00$calculatedId"
                                2 ->
                                    "FBO0$calculatedId"
                                else ->
                                    "FBO$calculatedId"
                            }
                        } else {
                            calulatedStringId = oldFontId
                        }
                        //Delete old image
                        deleteImageBeforeUploading(oldFontId)
                        if (hasImage) {
                            //Upload image
                            pujarImatge(calulatedStringId)
                        }

                        db.collection("fonts").document(calulatedStringId)
                            .set(
                                hashMapOf(
                                    "lat" to binding.editLat.text.toString().toDouble(),
                                    "lon" to binding.editLon.text.toString().toDouble(),
                                    "id" to calulatedStringId,
                                    "name" to binding.txtNomFont.text.toString(),
                                    "info" to binding.txtInformacio.text.toString(),
                                    "address" to binding.editAdreca.text.toString(),
                                    "type" to myFontType
                                )
                            )
                    }
                findNavController().navigate(EditFontFragmentDirections.actionEditFontFragmentToAdminFragment())
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
        var alertDialog: AlertDialog = objectAlerDialog.create()
        alertDialog.show()
    }

    private fun showBlankFields() {
        val objectAlerDialog = AlertDialog.Builder(context)
        objectAlerDialog.setTitle(R.string.error)
        objectAlerDialog.setMessage(R.string.camps_no_omplerts)
        objectAlerDialog.setPositiveButton(R.string.acceptar, null)
        var alertDialog: AlertDialog = objectAlerDialog.create()
        alertDialog.show()
    }

    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.data
            binding?.imgFont?.setImageURI(data)
            hasImage = true
            Snackbar.make(
                binding.frameLayout,
                R.string.imatge_pujada_correctament,
                BaseTransientBottomBar.LENGTH_SHORT
            ).show()
        }
    }

    private fun openGallery() {
        val intent: Intent? = Intent(Intent.ACTION_PICK)
        if (intent != null) {
            intent.type = "image/*"
        }
        startForActivityGallery?.launch(intent)
    }

    private fun pujarImatge(fontId: String?) {
        val imgPath = "images/" + fontId + ".jpg"
        val pathReference = storageRef?.child(imgPath)
        val bitmap: Bitmap? = (imageView?.drawable as BitmapDrawable)?.bitmap
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos?.toByteArray()

        val uploadTask = pathReference?.putBytes(data)
        uploadTask.addOnFailureListener {
            //Error al pujar
        }.addOnSuccessListener {
            //Pujat correctament
        }
    }

    private fun descarregarImatgeGlide(fontId: String?) {
        val imgPath = "images/" + fontId + ".jpg"
        val imageRef = storageRef?.child(imgPath)
        imageRef?.downloadUrl?.addOnSuccessListener { url ->

            Glide.with(this)
                .load(url.toString())
                .centerInside()
                .placeholder((R.drawable.loading))
                .error(R.drawable.ic_noimage)
                .into(imageView)
            hasImage = true

        }.addOnFailureListener {
            binding.imgFont?.setImageResource(R.drawable.ic_noimage)
            hasImage = false
        }
    }

    private fun deleteImage(fontId: String?) {
        val imgPath = "images/" + fontId + ".jpg"
        val imageRef = storageRef?.child(imgPath)
        Log.d("msg", "arriba1")
        imageRef?.delete()?.addOnSuccessListener {
            // File deleted successfully
        }.addOnFailureListener {
            // Filed to remove the image
        }
        Log.d("msg", "arriba2")
        binding?.imgFont?.setImageResource(R.drawable.ic_noimage)
        Log.d("msg", "arriba3")
    }

    private fun deleteImageBeforeUploading(fontId: String) {
        val imgPath = "images/" + fontId + ".jpg"
        val imageRef = storageRef?.child(imgPath)
        imageRef?.delete().addOnSuccessListener {
            // File deleted successfully
        }.addOnFailureListener {
            // Filed to remove the image
        }
    }
}
