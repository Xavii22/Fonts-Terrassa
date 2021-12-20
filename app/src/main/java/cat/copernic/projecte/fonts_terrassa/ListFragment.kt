package cat.copernic.projecte.fonts_terrassa

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import cat.copernic.projecte.fonts_terrassa.ViewModel.ListViewModel
import cat.copernic.projecte.fonts_terrassa.adapters.FontRecyclerAdapter
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentListBinding
import cat.copernic.projecte.fonts_terrassa.models.Font
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

class ListFragment : Fragment() {

    private var fonts: ArrayList<Font> = arrayListOf()
    private var matchedFonts: ArrayList<Font> = arrayListOf()
    private var fontAdapter: FontRecyclerAdapter = FontRecyclerAdapter(fonts)
    private val db = FirebaseFirestore.getInstance()

    private lateinit var binding: FragmentListBinding
    private val ViewModel: ListViewModel by viewModels()
    private var fontsArray = arrayOf<String>()
    lateinit var imageView: ImageView
    lateinit var selectedFont: BooleanArray
    private var fontList: ArrayList<Int> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list, container, false
        )

        fontsArray = arrayOf(resources.getString(R.string.fonts_boca), resources.getString(R.string.fonts_boca_singulars),
                resources.getString(R.string.fonts_ornamentals), resources.getString(R.string.fonts_naturals),
                resources.getString(R.string.fonts_gossos))

        imageView = binding.imageView
        selectedFont = BooleanArray(fontsArray.size)

        for (j in 0..4) {
            selectedFont[j] = true
        }

        if (fontAdapter.fonts.size == 0) {
            context?.let { ViewModel.filterFontsByType(binding, it, selectedFont) }
        }

        imageView.setOnClickListener {

            fontList.clear()

            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.seleccionar_tipus_font)

            context?.let { ViewModel.clearFontsByType(binding, it) }
            builder.setCancelable(false)
            builder.setMultiChoiceItems(
                fontsArray, selectedFont
            ) { _, _, _ ->

            }
            builder.setPositiveButton(
                R.string.acceptar
            ) { _, _ ->
                context?.let { ViewModel.filterFontsByType(binding, it, selectedFont) }
            }
            builder.setNeutralButton(
                R.string.seleccionar_tot
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
                id: Long,
            ) {
                when (binding.spinnerOrder.selectedItem.toString()) {
                    resources.getString(R.string.nom_asc) ->
                        context?.let {
                            ViewModel.sortFontNameASC(binding, it)
                        }
                    resources.getString(R.string.nom_desc) ->
                        context?.let {
                            ViewModel.sortFontNameDESC(binding, it)
                        }
                    resources.getString(R.string.distancia_asc) ->
                        context?.let {
                            ViewModel.sortFontLocationASC(binding, it)
                        }
                    resources.getString(R.string.distancia_desc) ->
                        context?.let {
                            ViewModel.sortFontLocationDESC(binding, it)
                        }
                    resources.getString(R.string.tipus) ->
                        context?.let {
                            ViewModel.sortFontTypeASC(binding, it)
                        }
                }
            }
        }

        loadData()
        performSearch()
        return binding.root
    }

    override fun onResume() {
        loadData()
        super.onResume()
    }

    private fun initRecyclerView() {
        fonts = ViewModel.getFonts()

        fontAdapter = FontRecyclerAdapter(fonts).also {
            binding.rvFonts.adapter = it
            binding.rvFonts.adapter!!.notifyDataSetChanged()
        }
        binding.svFonts.isSubmitButtonEnabled = true
    }

    private fun loadData() = GlobalScope.launch(Dispatchers.Main) {
        val dialogJob = launch {
            delay(1000)
            try {
                showLoadingDialog()
                coroutineContext.job.join()
            } finally {
                hideLoadingDialog()
            }
        }
        initRecyclerView()
        dialogJob.cancel()
    }

    private fun showLoadingDialog(){
        binding.loadingAnimation.visibility = View.VISIBLE
    }

    private fun hideLoadingDialog(){
        binding.loadingAnimation.visibility = View.GONE
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
        fonts.clear()
        db.collection("fonts").get().addOnSuccessListener { documents ->
            for (document in documents) {
                fonts.add(
                    Font(
                        document.get("id").toString(),
                        document.get("name").toString(),
                        document.get("lat").toString().toDouble(),
                        document.get("lon").toString().toDouble(),
                        document.get("info").toString(),
                        document.get("type").toString().toInt(),
                        document.get("address").toString(),
                        0.0
                    )
                )
            }
            text?.let {
                fonts.forEach { font ->
                    if (font.name.contains(text, true)
                    ) {
                        matchedFonts.add(font)
                    }
                }
                if (matchedFonts.isEmpty()) {
                    Log.d("buit", R.string.buit.toString())
                }
                updateRecyclerView()
            }
        }
    }

    private fun updateRecyclerView() {
        binding.rvFonts.apply {
            fontAdapter.fonts.clear()
            fontAdapter.fonts.addAll(matchedFonts)
            context?.let { ViewModel.sortFontNameASC(binding, it) }
            fontAdapter.notifyDataSetChanged()
        }
    }
}
