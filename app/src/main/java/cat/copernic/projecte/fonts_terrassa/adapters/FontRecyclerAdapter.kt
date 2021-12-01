package cat.copernic.projecte.fonts_terrassa.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.projecte.fonts_terrassa.R
import cat.copernic.projecte.fonts_terrassa.databinding.ItemFontListBinding
import cat.copernic.projecte.fonts_terrassa.models.Font
import android.os.Bundle
import androidx.navigation.findNavController
import kotlin.collections.ArrayList
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FontRecyclerAdapter :
    RecyclerView.Adapter<FontRecyclerAdapter.ViewHolder>() {
    private var fonts: MutableList<Font> = ArrayList()
    private lateinit var fontsOriginal: MutableList<Font>

    lateinit var context: Context

    init {

        //fontsOriginal.addAll(fonts)
    }

    //constructor de la classe on es passa la font de dades i el context sobre el que es mostrarà
    fun fontsRecyclerAdapter(fontsList: MutableList<Font>, contxt: Context) {
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
            bundle.putSerializable("font_name", fonts[position].fontName)
            bundle.putSerializable("font_lat", fonts[position].lat.toString())
            bundle.putSerializable("font_lon", fonts[position].lon.toString())
            bundle.putSerializable("font_info", fonts[position].info)
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
/*
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    countryFilterList = countryList as ArrayList<Font>
                } else {
                    val resultList = ArrayList<Font>()
                    for (row in countryList) {
                        if (row.name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            resultList.add(row)
                        }
                    }
                    countryFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = countryFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                countryFilterList = results?.values as ArrayList<Font>
                notifyDataSetChanged()
            }
        }
    }*/
}