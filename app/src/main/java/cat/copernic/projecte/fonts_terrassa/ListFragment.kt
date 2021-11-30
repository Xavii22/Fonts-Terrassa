package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.projecte.fonts_terrassa.adapters.FontRecyclerAdapter
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentListBinding
import cat.copernic.projecte.fonts_terrassa.models.Font
import com.google.firebase.firestore.FirebaseFirestore
import android.view.*
import kotlin.collections.ArrayList
import android.widget.AdapterView
import android.widget.SearchView

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentListBinding
    private val myAdapter: FontRecyclerAdapter = FontRecyclerAdapter()
    private val db = FirebaseFirestore.getInstance()
    private var fonts: ArrayList<Font> = arrayListOf()
    var txtBuscar: SearchView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list, container, false
        )

        txtBuscar = binding.svFonts

        /*
        db.collection("fonts")
            .get()
            .addOnSuccessListener { documents ->
                fonts.clear()
                for (document in documents) {
                    fonts.add(
                        Font(
                            document.get("name").toString(),
                            document.get("lat").toString().toDouble(),
                            document.get("lon").toString().toDouble(),
                            document.get("type").toString().toInt()
                        )
                    )
                }
                binding.rvFonts.setHasFixedSize(true)
                binding.rvFonts.layoutManager = LinearLayoutManager(context)
                context?.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                binding.rvFonts.adapter = myAdapter
            }*/

        binding.spinnerOrder.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (binding.spinnerOrder.selectedItem.toString()) {
                    "Nom ASC" ->
                        db.collection("fonts")
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    fonts.sortBy { it.fontName }
                                }
                                binding.rvFonts.setHasFixedSize(true)
                                binding.rvFonts.layoutManager = LinearLayoutManager(context)
                                context?.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                                binding.rvFonts.adapter = myAdapter
                            }
                    "Nom DESC" ->
                        db.collection("fonts")
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    fonts.sortByDescending { it.fontName }
                                }
                                binding.rvFonts.setHasFixedSize(true)
                                binding.rvFonts.layoutManager = LinearLayoutManager(context)
                                context?.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                                binding.rvFonts.adapter = myAdapter
                            }
                    "Tipus ASC" ->
                        db.collection("fonts")
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    fonts.sortBy { it.fontType }
                                }
                                binding.rvFonts.setHasFixedSize(true)
                                binding.rvFonts.layoutManager = LinearLayoutManager(context)
                                context?.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                                binding.rvFonts.adapter = myAdapter
                            }
                    "Tipus DESC" ->
                        db.collection("fonts")
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    fonts.sortByDescending { it.fontType }
                                }
                                binding.rvFonts.setHasFixedSize(true)
                                binding.rvFonts.layoutManager = LinearLayoutManager(context)
                                context?.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                                binding.rvFonts.adapter = myAdapter
                            }
                }
            }
        }

        binding.spinnerFilter.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (binding.spinnerFilter.selectedItem.toString()) {
                        "Fonts de beure" ->
                            db.collection("fonts").whereEqualTo("type", 1)
                                .get()
                                .addOnSuccessListener { documents ->
                                    fonts.clear()
                                    for (document in documents) {
                                        fonts.add(
                                            Font(
                                                document.get("name").toString(),
                                                document.get("lat").toString().toDouble(),
                                                document.get("lon").toString().toDouble(),
                                                document.get("info").toString(),
                                                1
                                            )
                                        )
                                    }
                                    binding.rvFonts.setHasFixedSize(true)
                                    binding.rvFonts.layoutManager = LinearLayoutManager(context)
                                    context?.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                                    binding.rvFonts.adapter = myAdapter

                                }
                        "Fonts de beure singulars" ->
                            db.collection("fonts").whereEqualTo("type", 2)
                                .get()
                                .addOnSuccessListener { documents ->
                                    fonts.clear()
                                    for (document in documents) {
                                        fonts.add(
                                            Font(
                                                document.get("name").toString(),
                                                document.get("lat").toString().toDouble(),
                                                document.get("lon").toString().toDouble(),
                                                document.get("info").toString(),
                                                2
                                            )
                                        )
                                    }
                                    binding.rvFonts.setHasFixedSize(true)
                                    binding.rvFonts.layoutManager = LinearLayoutManager(context)
                                    context?.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                                    binding.rvFonts.adapter = myAdapter
                                }
                        "Fonts ornamentals" ->
                            db.collection("fonts").whereEqualTo("type", 3)
                                .get()
                                .addOnSuccessListener { documents ->
                                    fonts.clear()
                                    for (document in documents) {
                                        fonts.add(
                                            Font(
                                                document.get("name").toString(),
                                                document.get("lat").toString().toDouble(),
                                                document.get("lon").toString().toDouble(),
                                                document.get("info").toString(),
                                                3
                                            )
                                        )
                                    }
                                    binding.rvFonts.setHasFixedSize(true)
                                    binding.rvFonts.layoutManager = LinearLayoutManager(context)
                                    context?.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                                    binding.rvFonts.adapter = myAdapter
                                }
                        "Fonts naturals" ->
                            db.collection("fonts").whereEqualTo("type", 4)
                                .get()
                                .addOnSuccessListener { documents ->
                                    fonts.clear()
                                    for (document in documents) {
                                        fonts.add(
                                            Font(
                                                document.get("name").toString(),
                                                document.get("lat").toString().toDouble(),
                                                document.get("lon").toString().toDouble(),
                                                document.get("info").toString(),
                                                4
                                            )
                                        )
                                    }
                                    binding.rvFonts.setHasFixedSize(true)
                                    binding.rvFonts.layoutManager = LinearLayoutManager(context)
                                    context?.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                                    binding.rvFonts.adapter = myAdapter
                                }
                        "Fonts de gossos" ->
                            db.collection("fonts").whereEqualTo("type", 5)
                                .get()
                                .addOnSuccessListener { documents ->
                                    fonts.clear()
                                    for (document in documents) {
                                        fonts.add(
                                            Font(
                                                document.get("name").toString(),
                                                document.get("lat").toString().toDouble(),
                                                document.get("lon").toString().toDouble(),
                                                document.get("info").toString(),
                                                5
                                            )
                                        )
                                    }
                                    binding.rvFonts.setHasFixedSize(true)
                                    binding.rvFonts.layoutManager = LinearLayoutManager(context)
                                    context?.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                                    binding.rvFonts.adapter = myAdapter
                                }
                    }
                }
            }

        txtBuscar!!.setOnQueryTextListener(this)

        return binding.root
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        TODO("Not yet implemented")
    }

}
