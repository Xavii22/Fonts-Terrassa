package cat.copernic.projecte.fonts_terrassa.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.projecte.fonts_terrassa.adapters.FontRecyclerAdapter
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentListBinding
import cat.copernic.projecte.fonts_terrassa.models.Font
import com.google.firebase.firestore.FirebaseFirestore

class ListViewModel : ViewModel() {

    private val myAdapter: FontRecyclerAdapter = FontRecyclerAdapter()
    private var fonts: ArrayList<Font> = arrayListOf()
    private val db = FirebaseFirestore.getInstance()

    fun sortFontNameASC(binding: FragmentListBinding, context: Context) {
        db.collection("fonts")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    fonts.sortBy { it.fontName }
                }
                binding.rvFonts.setHasFixedSize(true)
                binding.rvFonts.layoutManager = LinearLayoutManager(context)
                context.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                binding.rvFonts.adapter = myAdapter
            }
    }

    fun sortFontNameDESC(binding: FragmentListBinding, context: Context) {
        db.collection("fonts")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    fonts.sortByDescending { it.fontName }
                }
                binding.rvFonts.setHasFixedSize(true)
                binding.rvFonts.layoutManager = LinearLayoutManager(context)
                context.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                binding.rvFonts.adapter = myAdapter
            }
    }

    fun sortFontTypeASC(binding: FragmentListBinding, context: Context) {
        db.collection("fonts")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    fonts.sortBy { it.fontType }
                }
                binding.rvFonts.setHasFixedSize(true)
                binding.rvFonts.layoutManager = LinearLayoutManager(context)
                context.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                binding.rvFonts.adapter = myAdapter
            }
    }

    fun sortFontTypeDESC(binding: FragmentListBinding, context: Context) {
        db.collection("fonts")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    fonts.sortByDescending { it.fontType }
                }
                binding.rvFonts.setHasFixedSize(true)
                binding.rvFonts.layoutManager = LinearLayoutManager(context)
                context.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                binding.rvFonts.adapter = myAdapter
            }
    }

    fun filterFontsByType(
        binding: FragmentListBinding,
        context: Context,
        fontEnabled: ArrayList<Int>
    ) {
        for (i in fontEnabled) {
            db.collection("fonts").whereEqualTo("type", i)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        fonts.add(
                            Font(
                                document.get("name").toString(),
                                document.get("lat").toString().toDouble(),
                                document.get("lon").toString().toDouble(),
                                document.get("info").toString(),
                                i
                            )
                        )
                    }
                    binding.rvFonts.setHasFixedSize(true)
                    binding.rvFonts.layoutManager = LinearLayoutManager(context)
                    context.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                    binding.rvFonts.adapter = myAdapter
                }
        }

    }

    fun clearFontsByType(binding: FragmentListBinding, context: Context) {
        db.collection("fonts")
            .get()
            .addOnSuccessListener {
                fonts.clear()
                binding.rvFonts.setHasFixedSize(true)
                binding.rvFonts.layoutManager = LinearLayoutManager(context)
                context.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                binding.rvFonts.adapter = myAdapter
            }
    }
}