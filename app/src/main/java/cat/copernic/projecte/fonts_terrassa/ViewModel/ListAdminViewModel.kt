package cat.copernic.projecte.fonts_terrassa.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.projecte.fonts_terrassa.adapters.FontAdminRecyclerAdapter
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentFontAdminListBinding
import cat.copernic.projecte.fonts_terrassa.models.Font
import com.google.firebase.firestore.FirebaseFirestore

class ListAdminViewModel: ViewModel() {

    private val myAdapter: FontAdminRecyclerAdapter = FontAdminRecyclerAdapter()
    private var fonts: ArrayList<Font> = arrayListOf()
    private val db = FirebaseFirestore.getInstance()

    fun sortFontNameASC(binding: FragmentFontAdminListBinding, context: Context) {
        db.collection("fonts")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    fonts.sortBy { it.fontName }
                }
                binding.rvFonts.setHasFixedSize(true)
                binding.rvFonts.layoutManager = LinearLayoutManager(context)
                context.let { myAdapter.fontsAdminRecyclerAdapter(fonts, it) }
                binding.rvFonts.adapter = myAdapter
            }
    }

    fun sortFontNameDESC(binding: FragmentFontAdminListBinding, context: Context) {
        db.collection("fonts")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    fonts.sortByDescending { it.fontName }
                }
                binding.rvFonts.setHasFixedSize(true)
                binding.rvFonts.layoutManager = LinearLayoutManager(context)
                context.let { myAdapter.fontsAdminRecyclerAdapter(fonts, it) }
                binding.rvFonts.adapter = myAdapter
            }
    }

    fun sortFontTypeASC(binding: FragmentFontAdminListBinding, context: Context) {
        db.collection("fonts")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    fonts.sortBy { it.fontType }
                }
                binding.rvFonts.setHasFixedSize(true)
                binding.rvFonts.layoutManager = LinearLayoutManager(context)
                context.let { myAdapter.fontsAdminRecyclerAdapter(fonts, it) }
                binding.rvFonts.adapter = myAdapter
            }
    }

    fun sortFontTypeDESC(binding: FragmentFontAdminListBinding, context: Context) {
        db.collection("fonts")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    fonts.sortByDescending { it.fontType }
                }
                binding.rvFonts.setHasFixedSize(true)
                binding.rvFonts.layoutManager = LinearLayoutManager(context)
                context.let { myAdapter.fontsAdminRecyclerAdapter(fonts, it) }
                binding.rvFonts.adapter = myAdapter
            }
    }

    fun filterFontsByType(
        binding: FragmentFontAdminListBinding,
        context: Context,
        fontEnabled: BooleanArray
    ) {
        var fontType: Int = -1
        for (i in 0..4) {
            when (fontEnabled[i]) {
                true ->
                    fontType = i + 1
                false ->
                    fontType = -1
            }
            db.collection("fonts").whereEqualTo("type", fontType)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        fonts.add(
                            Font(
                                document.get("id").toString(),
                                document.get("name").toString(),
                                document.get("lat").toString().toDouble(),
                                document.get("lon").toString().toDouble(),
                                document.get("info").toString(),
                                fontType,
                                document.get("address").toString()
                            )
                        )
                    }
                    binding.rvFonts.setHasFixedSize(true)
                    binding.rvFonts.layoutManager = LinearLayoutManager(context)
                    context.let { myAdapter.fontsAdminRecyclerAdapter(fonts, it) }
                    binding.rvFonts.adapter = myAdapter
                }
        }
    }

    fun clearFontsByType(binding: FragmentFontAdminListBinding, context: Context) {
        db.collection("fonts")
            .get()
            .addOnSuccessListener {
                fonts.clear()
                binding.rvFonts.setHasFixedSize(true)
                binding.rvFonts.layoutManager = LinearLayoutManager(context)
                context.let { myAdapter.fontsAdminRecyclerAdapter(fonts, it) }
                binding.rvFonts.adapter = myAdapter
            }
    }
}