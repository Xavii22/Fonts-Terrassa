package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.projecte.fonts_terrassa.adapters.FontRecyclerAdapter
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentListBinding
import cat.copernic.projecte.fonts_terrassa.models.Font
import com.google.firebase.firestore.FirebaseFirestore

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private val myAdapter: FontRecyclerAdapter = FontRecyclerAdapter()
    private val db = FirebaseFirestore.getInstance()
    private var fonts: ArrayList<Font> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

       binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list, container, false)


        when(binding.spinnerFilter.selectedItem.toString()){
            "Fonts 1" ->
                db.collection("fonts").whereEqualTo("type", 1)
                    .get()
                    .addOnSuccessListener{ documents ->
                        for (document in documents){
                            fonts.add(Font(
                                document.get("name").toString(),
                                document.get("lat").toString().toDouble(),
                                document.get("lon").toString().toDouble(),
                                1))
                        }
                        binding.rvFonts.setHasFixedSize(true)
                        binding.rvFonts.layoutManager = LinearLayoutManager(context)
                        context?.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                        binding.rvFonts.adapter = myAdapter
                    }
            "Fonts 2" ->
                db.collection("fonts").whereEqualTo("type", 1)
                    .get()
                    .addOnSuccessListener{ documents ->
                        for (document in documents){
                            fonts.add(Font(
                                document.get("name").toString(),
                                document.get("lat").toString().toDouble(),
                                document.get("lon").toString().toDouble(),
                                1))
                        }
                        binding.rvFonts.setHasFixedSize(true)
                        binding.rvFonts.layoutManager = LinearLayoutManager(context)
                        context?.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                        binding.rvFonts.adapter = myAdapter
                    }
            "Fonts 3" ->
                db.collection("fonts").whereEqualTo("type", 1)
                    .get()
                    .addOnSuccessListener{ documents ->
                        for (document in documents){
                            fonts.add(Font(
                                document.get("name").toString(),
                                document.get("lat").toString().toDouble(),
                                document.get("lon").toString().toDouble(),
                                1))
                        }
                        binding.rvFonts.setHasFixedSize(true)
                        binding.rvFonts.layoutManager = LinearLayoutManager(context)
                        context?.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                        binding.rvFonts.adapter = myAdapter
                    }
            "Fonts 4" ->
                db.collection("fonts").whereEqualTo("type", 1)
                    .get()
                    .addOnSuccessListener{ documents ->
                        for (document in documents){
                            fonts.add(Font(
                                document.get("name").toString(),
                                document.get("lat").toString().toDouble(),
                                document.get("lon").toString().toDouble(),
                                1))
                        }
                        binding.rvFonts.setHasFixedSize(true)
                        binding.rvFonts.layoutManager = LinearLayoutManager(context)
                        context?.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                        binding.rvFonts.adapter = myAdapter
                    }
            "Fonts 5" ->
                db.collection("fonts").whereEqualTo("type", 1)
                    .get()
                    .addOnSuccessListener{ documents ->
                        for (document in documents){
                            fonts.add(Font(
                                document.get("name").toString(),
                                document.get("lat").toString().toDouble(),
                                document.get("lon").toString().toDouble(),
                                1))
                        }
                        binding.rvFonts.setHasFixedSize(true)
                        binding.rvFonts.layoutManager = LinearLayoutManager(context)
                        context?.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                        binding.rvFonts.adapter = myAdapter
                    }
        }

        return binding.root
    }
}