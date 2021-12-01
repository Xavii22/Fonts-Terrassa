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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import cat.copernic.projecte.fonts_terrassa.ViewModel.ListViewModel

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentListBinding
    private val ViewModel: ListViewModel by viewModels()
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
                        context?.let {
                            ViewModel.sortFontNameASC(binding, it)
                        }

                    "Nom DESC" ->
                        context?.let {
                            ViewModel.sortFontNameDESC(binding, it)
                        }
                    "Tipus ASC" ->
                        context?.let {
                            ViewModel.sortFontTypeASC(binding, it)
                        }
                    "Tipus DESC" ->
                        context?.let {
                            ViewModel.sortFontTypeDESC(binding, it)
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
                            context?.let { ViewModel.filterFontsByType(binding, it, 1) }
                        "Fonts de beure singulars" ->
                            context?.let { ViewModel.filterFontsByType(binding, it, 2) }
                        "Fonts ornamentals" ->
                            context?.let { ViewModel.filterFontsByType(binding, it, 3) }
                        "Fonts naturals" ->
                            context?.let { ViewModel.filterFontsByType(binding, it, 4) }
                        "Fonts de gossos" ->
                            context?.let { ViewModel.filterFontsByType(binding, it, 5) }
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

    /*
    binding.svFonts.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
        androidx.appcompat.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextChange(p0: String?): Boolean {
            TODO("Not yet implemented")
        }

        override fun onQueryTextSubmit(newText: String?): Boolean {
            tempFonts.clear()
            val searchText = newText!!.lowercase(Locale.getDefault())

            if (searchText.isNotEmpty()) {
                fonts.forEach {
                    if(it.heading.lowercase(Locale.getDefault()).contains(searchText)) {
                        tempFonts.add(it)
                    }
                }
                binding.rvFonts.adapter!!.notifyDataSetChanged()
            }else{
                tempFonts.clear()
                tempFonts.addAll(fonts)
                binding.rvFonts.adapter!!.notifyDataSetChanged()
            }

            return false
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        binding.svFonts.clearFocus()
/*
        if (fonts.contains(query)) {
            fonts.filtrat.filter(query)
        }*/
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        //fonts.filtrat.filter(newText)
        return false
    }
*/

}
