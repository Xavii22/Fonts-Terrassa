package cat.copernic.projecte.fonts_terrassa

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentListBinding
import android.view.*
import kotlin.collections.ArrayList
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.SearchView
import androidx.fragment.app.viewModels
import cat.copernic.projecte.fonts_terrassa.ViewModel.ListViewModel

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentListBinding
    private val ViewModel: ListViewModel by viewModels()
    var txtBuscar: SearchView? = null
    var fontsArray = arrayOf(
        "Fonts de beure", "Fonts de beure singulars", "Fonts ornamentals",
        "Font naturals", "Fonts de gossos"
    )
    lateinit var imageView: ImageView
    lateinit var selectedFont: BooleanArray
    private var fontList: ArrayList<Int> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list, container, false
        )

        txtBuscar = binding.svFonts
        imageView = binding.imageView
        selectedFont = BooleanArray(fontsArray.size)

        for (j in 0..4) {
            selectedFont[j] = true

        }

        context?.let { ViewModel.filterFontsByType(binding, it, selectedFont) }

        imageView.setOnClickListener {

            fontList.clear()

            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Seleccionar tipus de font")

            context?.let { ViewModel.clearFontsByType(binding, it) }
            builder.setCancelable(false)
            builder.setMultiChoiceItems(
                fontsArray, selectedFont
            ) { _, _, _ ->

            }
            builder.setPositiveButton(
                "Acceptar"
            ) { _, _ ->
                context?.let { ViewModel.filterFontsByType(binding, it, selectedFont) }
            }
            builder.show()
        }

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
