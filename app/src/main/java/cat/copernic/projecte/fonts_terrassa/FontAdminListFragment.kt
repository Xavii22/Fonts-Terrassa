package cat.copernic.projecte.fonts_terrassa

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import cat.copernic.projecte.fonts_terrassa.ViewModel.ListAdminViewModel
import cat.copernic.projecte.fonts_terrassa.adapters.FontRecyclerAdapter
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentFontAdminListBinding
import cat.copernic.projecte.fonts_terrassa.models.Font

class FontAdminListFragment : Fragment() {

    private var fonts: ArrayList<Font> = arrayListOf()
    private var matchedFonts: ArrayList<Font> = arrayListOf()
    private var fontAdapter: FontRecyclerAdapter = FontRecyclerAdapter(fonts)

    private lateinit var binding: FragmentFontAdminListBinding
    private val ViewModel: ListAdminViewModel by viewModels()
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
            inflater, R.layout.fragment_font_admin_list, container, false
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
            builder.setNeutralButton(
                "Seleccionar-ho tot"
            ) { _, _ ->
                for (j in 0..4) {
                    selectedFont[j] = true
                }
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
        initRecyclerView()
        performSearch()

        return binding.root
    }

    override fun onResume() {
        initRecyclerView()
        super.onResume()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView() {
        fonts = ViewModel.getFonts()

        fontAdapter = FontRecyclerAdapter(fonts).also {
            binding.rvFonts.adapter = it
            binding.rvFonts.adapter!!.notifyDataSetChanged()
        }
        binding.svFonts.isSubmitButtonEnabled = true
    }

    private fun performSearch() {
        binding.svFonts.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText)
                return true
            }
        })
    }

    private fun search(text: String?) {
        matchedFonts = arrayListOf()

        text?.let {
            fonts.forEach { font ->
                if (font.name.contains(text, true)
                ) {
                    matchedFonts.add(font)
                    updateRecyclerView()
                }
            }
            if (matchedFonts.isEmpty()) {
                Log.d("buit", "esta buit")
            }
            updateRecyclerView()
        }
    }

    private fun updateRecyclerView() {
        binding.rvFonts.apply {
            //fontAdapter.fonts.clear()
            //fontAdapter.fonts.addAll(matchedFonts)
            fontAdapter.fonts = matchedFonts
            fontAdapter.notifyDataSetChanged()
        }
    }
}
