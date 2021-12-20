package cat.copernic.projecte.fonts_terrassa.ViewModel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.projecte.fonts_terrassa.adapters.FontRecyclerAdapter
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentListBinding
import cat.copernic.projecte.fonts_terrassa.models.Font
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore

class ListViewModel : ViewModel() {

    private val myAdapter: FontRecyclerAdapter = FontRecyclerAdapter(arrayListOf())
    private var fonts: ArrayList<Font> = arrayListOf() //ArrayList<Font> = arrayListOf() | MutableLiveData<Font> = MutableLiveData<Font>()
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
                    fonts.sortBy { it.fontDistance }
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
                    fonts.sortByDescending { it.fontDistance }
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

    private lateinit var fusedLocationClient: FusedLocationProviderClient

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
            fonts.clear()
            db.collection("fonts").whereEqualTo("type", fontType)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        lateinit var myActualPos: Location
                        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {

                        }
                        fusedLocationClient.lastLocation
                            .addOnSuccessListener {
                                if (it != null) {
                                    myActualPos = it
                                    val fontLoc = Location("")
                                    fontLoc.latitude = document.get("lat").toString().toDouble()
                                    fontLoc.longitude = document.get("lon").toString().toDouble()
                                    val value = (myActualPos.distanceTo(fontLoc) / 1000).toDouble()
                                    fonts.add(
                                        Font(
                                            document.get("id").toString(),
                                            document.get("name").toString(),
                                            document.get("lat").toString().toDouble(),
                                            document.get("lon").toString().toDouble(),
                                            document.get("info").toString(),
                                            document.get("type").toString().toInt(),
                                            document.get("address").toString(),
                                            (Math.round(value * 100) / 100.0)
                                        )
                                    )
                                }
                            }
                    }
                    for (document in documents) {
                        fonts.sortBy { it.fontName }
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