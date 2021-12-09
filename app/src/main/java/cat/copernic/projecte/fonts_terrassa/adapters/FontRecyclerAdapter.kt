package cat.copernic.projecte.fonts_terrassa.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.projecte.fonts_terrassa.R
import cat.copernic.projecte.fonts_terrassa.databinding.ItemFontListBinding
import cat.copernic.projecte.fonts_terrassa.models.Font
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import java.util.logging.Filter
import java.util.logging.LogRecord
import kotlin.collections.ArrayList

class FontRecyclerAdapter(var fonts: ArrayList<Font>) :
    RecyclerView.Adapter<FontRecyclerAdapter.ViewHolder>() {

    //var fonts: ArrayList<Font> = ArrayList()
    //var fontsFiltered: ArrayList<Font> = ArrayList()
    val initialFontList = ArrayList<Font>().apply {
        fonts?.let { addAll(it) }
    }
    var onItemClick: ((Font) -> Unit)? = null
    lateinit var context: Context

    //constructor de la classe on es passa la font de dades i el context sobre el que es mostrarà
    fun fontsRecyclerAdapter(fontsList: ArrayList<Font>, contxt: Context) {
        this.fonts = fontsList
        this.context = contxt
    }

    //és l'encarregat de retornar el ViewHolder ja configurat
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            ItemFontListBinding.inflate(
                layoutInflater, parent, false
            )
        )
    }

    //Aquest mètode s'encarrega de passar els objectes, un a un al ViewHolder personalitzat
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            fonts[position]
        )

        with(holder) {
            with(fonts[position]) {
                binding.txtFont.text = this.fontName
                this.fontName?.let { descarregarImatgeGlide(context, it) }
            }
        }

        val item = fonts[position]
        holder.bind(item)

        //establim un listener
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("fontId", fonts[position].fontId)
            bundle.putSerializable("font_type", item.fontType)

            holder.itemView.findNavController().navigate(
                R.id.action_fragment_list_to_viewFontFragment, bundle
            )
        }
    }

    override fun getItemCount(): Int {
        return fonts.size
    }

    class ViewHolder(val binding: ItemFontListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(font: Font) {
            binding.txtFont.text = font.name.trim()
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
    }

    fun getFilter(): android.widget.Filter {
        return fontFilter
    }

    private val fontFilter = object : android.widget.Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: ArrayList<Font> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                initialFontList.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().toLowerCase()
                initialFontList.forEach {
                    if (it.name.lowercase(Locale.ROOT).contains(query)) {
                        filteredList.add(it)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results?.values is ArrayList<*>) {
                fonts.clear()
                fonts.addAll(results.values as ArrayList<Font>)
                notifyDataSetChanged()
            }
        }
    }
}