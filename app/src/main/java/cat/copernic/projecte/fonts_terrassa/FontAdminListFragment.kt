package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import cat.copernic.projecte.fonts_terrassa.ViewModel.ListAdminViewModel
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentFontAdminListBinding

class FontAdminListFragment : Fragment() {

    private lateinit var binding: FragmentFontAdminListBinding
    private val ViewModel: ListAdminViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_font_admin_list, container, false
        )

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

        return binding.root
    }

}