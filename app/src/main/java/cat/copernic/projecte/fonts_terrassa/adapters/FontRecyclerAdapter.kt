package cat.copernic.projecte.fonts_terrassa.adapters

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.projecte.fonts_terrassa.R
import cat.copernic.projecte.fonts_terrassa.databinding.ItemFontListBinding
import cat.copernic.projecte.fonts_terrassa.models.Font
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import kotlin.collections.ArrayList

class FontRecyclerAdapter(var fonts: ArrayList<Font>) :
    RecyclerView.Adapter<FontRecyclerAdapter.ViewHolder>() {

    //var fonts: ArrayList<Font> = ArrayList()
    //var fontsFiltered: ArrayList<Font> = ArrayList()
    val initialFontList = ArrayList<Font>().apply {
        addAll(fonts)
    }
    var context: Context? = null

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
                this.fontName?.let { context?.let { it1 -> descarregarImatgeGlide(it1, it) } }
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

    inner class ViewHolder(val binding: ItemFontListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(font: Font) {
            binding.txtFont.text = font.name.trim()
            binding.txtCarrer.text = font.adreca.trim()
            context?.let {
                this@FontRecyclerAdapter.calculateDistance(font.lat, font.lon, binding,
                    it)
            }

            Log.d("tipusf", font.type.toString())
            when (font.type) {
                1 -> binding.imageView2.setImageResource(R.drawable.gota_1)
                2 -> binding.imageView2.setImageResource(R.drawable.gota_2)
                3 -> binding.imageView2.setImageResource(R.drawable.gota_3)
                4 -> binding.imageView2.setImageResource(R.drawable.gota_4)
                5 -> binding.imageView2.setImageResource(R.drawable.gota_5)
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
                val query = constraint.toString().trim().lowercase(Locale.getDefault())
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
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    fun calculateDistance(lat: Double, lon: Double, binding: ItemFontListBinding, mycontext: Context) {
        lateinit var myActualPos: Location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mycontext)
        if (ActivityCompat.checkSelfPermission(mycontext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mycontext,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {

        }
        var value: Double = 0.0
        fusedLocationClient.lastLocation
            .addOnSuccessListener {
                    if (it != null) {
                        myActualPos = it
                        val fontLoc = Location("")
                        fontLoc.latitude = lat
                        fontLoc.longitude = lon
                        value = (myActualPos.distanceTo(fontLoc) / 1000).toDouble()
                        binding.txtDistance.text = (Math.round(value*100) /100.0).toString() + " km"
                    }
                }
    }
}