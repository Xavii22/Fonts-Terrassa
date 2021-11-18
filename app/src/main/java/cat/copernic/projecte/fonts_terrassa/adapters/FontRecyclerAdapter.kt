package cat.copernic.projecte.fonts_terrassa.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.projecte.fonts_terrassa.databinding.ItemFontListBinding
import cat.copernic.projecte.fonts_terrassa.models.Font


class FontRecyclerAdapter: RecyclerView.Adapter<FontRecyclerAdapter.ViewHolder>() {
    private var fonts: MutableList<Font> = ArrayList()
    lateinit var context: Context

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
            }
        }
        val item = fonts[position]
        holder.bind(item)

        //estamblim un listener
        holder.itemView.setOnClickListener {
            Toast.makeText(context, fonts[position].fontName, Toast.LENGTH_LONG).show()
        }
    }


    override fun getItemCount(): Int {
        return fonts.size
    }


    class ViewHolder(val binding: ItemFontListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(font: Font) {

        }
    }
}