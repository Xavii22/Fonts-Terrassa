package cat.copernic.projecte.fonts_terrassa.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.projecte.fonts_terrassa.R
import cat.copernic.projecte.fonts_terrassa.models.Font
import android.os.Bundle
import androidx.navigation.findNavController
import cat.copernic.projecte.fonts_terrassa.databinding.ItemFontListAdminBinding

class FontAdminRecyclerAdapter : RecyclerView.Adapter<FontAdminRecyclerAdapter.ViewHolder>() {
    private var fonts: MutableList<Font> = ArrayList()
    lateinit var context: Context

    //constructor de la classe on es passa la font de dades i el context sobre el que es mostrarà
    fun fontsAdminRecyclerAdapter(fontsList: MutableList<Font>, contxt: Context) {
        this.fonts = fontsList
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

    //Aquest mètode s'encarrega de passar els objectes, un a un al ViewHolder personalitzat
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            with(fonts[position]) {
                binding.txtFont.text = this.fontName
            }
        }
        val item = fonts[position]
        holder.bind(item)

        //estamblim un listener
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("font_name", fonts[position].fontName)
            bundle.putSerializable("font_lat", fonts[position].lat.toString())
            bundle.putSerializable("font_lon", fonts[position].lon.toString())
            holder.itemView.findNavController().navigate(
                R.id.action_adminFragment_to_fontAdminListFragment, bundle
            )
        }
    }

    override fun getItemCount(): Int {
        return fonts.size
    }

    class ViewHolder(val binding: ItemFontListAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(font: Font) {

        }
    }
}