package cat.copernic.projecte.fonts_terrassa.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.projecte.fonts_terrassa.adapters.FontRecyclerAdapter
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentListBinding
import cat.copernic.projecte.fonts_terrassa.models.Font
import com.google.firebase.firestore.FirebaseFirestore

class ListViewModel : ViewModel() {

    private val myAdapter: FontRecyclerAdapter = FontRecyclerAdapter(arrayListOf())
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

    fun sortFontLocationASC(binding: FragmentListBinding, context: Context) {
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

    fun sortFontLocationDESC(binding: FragmentListBinding, context: Context) {
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

    fun getFontType(
        fontEnabled: BooleanArray
    ): Array<Int> {
        var fontsType: Array<Int> = arrayOf(5)
        var fontType: Int = -1
        for (i in 0..4) {
            when (fontEnabled[i]) {
                true ->
                    fontType = i + 1
                false ->
                    fontType = -1
            }
            fontsType[i] = fontType
        }
        return fontsType
    }

    fun filterFontsByType(
        binding: FragmentListBinding,
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

    fun getFonts(): ArrayList<Font> {
        return fonts
    }
}