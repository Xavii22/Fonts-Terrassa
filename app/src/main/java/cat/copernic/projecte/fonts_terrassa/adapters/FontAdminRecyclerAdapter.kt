package cat.copernic.projecte.fonts_terrassa.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.projecte.fonts_terrassa.R
import cat.copernic.projecte.fonts_terrassa.databinding.ItemFontListAdminBinding
import cat.copernic.projecte.fonts_terrassa.models.Font
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FontAdminRecyclerAdapter : RecyclerView.Adapter<FontAdminRecyclerAdapter.ViewHolder>() {
    private var fontsAdmin: MutableList<Font> = ArrayList()
    lateinit var context: Context
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
                this.fontName?.let { descarregarImatgeGlide(context, it) }
            }
            //Listener Delete Button
            holder.itemView.findViewById<ImageView>(R.id.btnDeleteFont).setOnClickListener {
                val objectAlerDialog = AlertDialog.Builder(context)
                objectAlerDialog.setTitle("Confirmació")
                objectAlerDialog.setMessage("Eliminar Font")
                objectAlerDialog.setPositiveButton("Acceptar"){dialog, which ->
                    db.collection("fonts").document(fontsAdmin[position].name)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(context,"Font eliminada correctament", Toast.LENGTH_SHORT).show()

                        }.addOnFailureListener { e ->
                            Toast.makeText(context,"ERROR en eliminar la font", Toast.LENGTH_SHORT).show()
                        }
                    fontsAdmin[position].fontName?.let { it1 -> deleteImage(it1) }
                }
                objectAlerDialog.setNegativeButton("Descartar"){dialog, which ->
                    Toast.makeText(context,"La font no s'ha eliminat", Toast.LENGTH_SHORT).show()
                }
                var alertDialog: AlertDialog = objectAlerDialog.create()
                alertDialog.show()
                /*
                db.collection("fonts").document(fontsAdmin[position].name)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(context,"Font eliminada correctament", Toast.LENGTH_SHORT).show()

                    }.addOnFailureListener { e ->
                        Toast.makeText(context,"ERROR en eliminar la font", Toast.LENGTH_SHORT).show()
                    }
                fontsAdmin[position].fontName?.let { it1 -> deleteImage(it1) }
                 */
            }
        }
        val item = fontsAdmin[position]
        holder.bind(item)

        //estamblim un listener
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("font_name", fontsAdmin[position].fontName)
            bundle.putSerializable("font_lat", fontsAdmin[position].lat.toString())
            bundle.putSerializable("font_lon", fontsAdmin[position].lon.toString())
            bundle.putSerializable("font_info", fontsAdmin[position].info)
            bundle.putSerializable("font_type", fontsAdmin[position].type)
            holder.itemView.findNavController().navigate(
                R.id.action_fontAdminListFragment_to_editFontFragment2, bundle
            )
        }
    }

    override fun getItemCount(): Int {
        return fontsAdmin.size
    }

    class ViewHolder(val binding: ItemFontListAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(font: Font) {

        }

        private val storageRef: StorageReference = FirebaseStorage.getInstance().reference
        fun descarregarImatgeGlide(view: Context, fontId: String) {
            val imgPath = "images/" + fontId + ".jpg"
            val imageRef = storageRef.child(imgPath)
            imageRef.downloadUrl.addOnSuccessListener { url ->

                Glide.with(view)
                    .load(url.toString())
                    .centerInside()
                    .placeholder((R.drawable.loading))
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