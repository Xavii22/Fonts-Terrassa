package cat.copernic.projecte.fonts_terrassa.adapters

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.projecte.fonts_terrassa.FontAdminListFragment
import cat.copernic.projecte.fonts_terrassa.R
import cat.copernic.projecte.fonts_terrassa.databinding.ItemFontListAdminBinding
import cat.copernic.projecte.fonts_terrassa.models.Font
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FontAdminRecyclerAdapter : RecyclerView.Adapter<FontAdminRecyclerAdapter.ViewHolder>() {
    private var fontsAdmin: MutableList<Font> = ArrayList()
    var context: Context? = null
    private val db = FirebaseFirestore.getInstance()

    //constructor de la classe on es passa la font de dades i el context sobre el que es mostrarà
    fun fontsAdminRecyclerAdapter(fontsList: MutableList<Font>, contxt: Context) {
        this.fontsAdmin = fontsList
        this.context = contxt
    }

    //és l'encarregat de retornar el ViewHolder ja configurat
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            ItemFontListAdminBinding.inflate(
                layoutInflater, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            with(fontsAdmin[position]) {
                binding.txtFont.text = this.fontName
                this.fontId?.let { context?.let { it1 -> descarregarImatgeGlide(it1, it) } }
            }
            //Listener Delete Button
            holder.itemView.findViewById<ImageView>(R.id.btnDeleteFont).setOnClickListener {
                val objectAlerDialog = AlertDialog.Builder(context)
                objectAlerDialog.setTitle("@string/")
                objectAlerDialog.setMessage("@string/eliminar_font")
                objectAlerDialog.setPositiveButton("@string/acceptar") { dialog, which ->
                    db.collection("fonts").document(fontsAdmin[position].id)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "@string/font_eliminada",
                                Toast.LENGTH_SHORT
                            ).show()
                            fontsAdmin.removeAt(position)
                            notifyDataSetChanged()
                        }.addOnFailureListener { e ->
                            Toast.makeText(context, "@string/error_eliminar_font", Toast.LENGTH_SHORT)
                                .show()
                        }
                    fontsAdmin[position].fontId?.let { it1 -> deleteImage(it1) }
                }
                objectAlerDialog.setNegativeButton("@string/descartar") { dialog, which ->
                    Toast.makeText(context, "@string/font_no_eliminada", Toast.LENGTH_SHORT).show()
                }
                var alertDialog: AlertDialog = objectAlerDialog.create()
                alertDialog.show()
            }
        }
        val item = fontsAdmin[position]
        holder.bind(item)

        //estamblim un listener
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("id_font", fontsAdmin[position].fontId)
            bundle.putSerializable("font_name", fontsAdmin[position].fontName)
            bundle.putSerializable("font_lat", fontsAdmin[position].lat.toString())
            bundle.putSerializable("font_lon", fontsAdmin[position].lon.toString())
            bundle.putSerializable("font_info", fontsAdmin[position].info)
            bundle.putSerializable("font_adreca", fontsAdmin[position].adreca)
            bundle.putSerializable("font_type", fontsAdmin[position].type)
            holder.itemView.findNavController().navigate(
                R.id.action_fontAdminListFragment_to_editFontFragment2, bundle
            )
        }
    }

    override fun getItemCount(): Int {
        return fontsAdmin.size
    }

    inner class ViewHolder(val binding: ItemFontListAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(font: Font) {
            binding.txtFont.text = font.name.trim()

            when (font.type) {
                1 -> context?.let { descarregarImatgeGlide2(it, "gota_1") }
                2 -> context?.let { descarregarImatgeGlide2(it, "gota_2") }
                3 -> context?.let { descarregarImatgeGlide2(it, "gota_3") }
                4 -> context?.let { descarregarImatgeGlide2(it, "gota_4") }
                5 -> context?.let { descarregarImatgeGlide2(it, "gota_5") }
            }
        }

        fun descarregarImatgeGlide2(view: Context, fontId: String) {
            val imgPath = fontId + ".png"
            val imageRef = storageRef.child(imgPath)
            imageRef.downloadUrl.addOnSuccessListener { url ->

                Glide.with(view)
                    .load(url.toString())
                    .centerInside()
                    .error(R.drawable.ic_noimage)
                    .into(binding.imageView2)

            }.addOnFailureListener {
                binding.imageView2.setImageResource(R.drawable.ic_noimage)
            }
        }

        private val storageRef: StorageReference = FirebaseStorage.getInstance().reference

        fun descarregarImatgeGlide(view: Context, fontId: String) {
            val imgPath = "images/" + fontId + ".jpg"
            val imageRef = storageRef.child(imgPath)
            imageRef.downloadUrl.addOnSuccessListener { url ->

                Glide.with(view)
                    .load(url.toString())
                    .centerInside()
                    .error(R.drawable.ic_noimage)
                    .into(binding.imageView)

            }.addOnFailureListener {
                binding.imageView.setImageResource(R.drawable.ic_noimage)
            }
        }

        fun deleteImage(fontId: String) {
            val imgPath = "images/" + fontId + ".jpg"
            val imageRef = storageRef.child(imgPath)
            imageRef.delete().addOnSuccessListener {
                // File deleted successfully
            }.addOnFailureListener {
                // Filed to remove the image
            }
        }
    }
}