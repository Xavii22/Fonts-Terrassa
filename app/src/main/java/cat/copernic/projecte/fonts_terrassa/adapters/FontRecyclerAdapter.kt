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
import kotlin.collections.ArrayList

class FontRecyclerAdapter(var fonts: ArrayList<Font>) :
    RecyclerView.Adapter<FontRecyclerAdapter.ViewHolder>() {

    lateinit var context: Context
    private lateinit var arrayTypeFonts: Array<Int>

    fun fontsRecyclerAdapter(fontsList: ArrayList<Font>, contxt: Context) {
        this.fonts = fontsList
        this.context = contxt
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            ItemFontListBinding.inflate(
                layoutInflater, parent, false
            )
        )
    }

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
            binding.txtCarrer.text = font.adreca.trim()

            for (i in 0..4) {

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

}