package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.projecte.fonts_terrassa.adapters.FontAdminRecyclerAdapter
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentFontAdminListBinding
import cat.copernic.projecte.fonts_terrassa.databinding.ItemFontListAdminBinding
import cat.copernic.projecte.fonts_terrassa.models.Font
import com.google.firebase.firestore.FirebaseFirestore

class FontAdminListFragment : Fragment() {

    private lateinit var binding: FragmentFontAdminListBinding
    //private lateinit var binding2: ItemFontListAdminBinding
    private val myAdapter: FontAdminRecyclerAdapter = FontAdminRecyclerAdapter()
    private val db = FirebaseFirestore.getInstance()
    private var fonts: ArrayList<Font> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_font_admin_list, container, false
        )
        /*
        binding2 = DataBindingUtil.inflate(
            inflater, R.layout.fragment_font_admin_list, container, false
        )
        */
        db.collection("fonts").whereEqualTo("type", 1)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    fonts.add(
                        Font(
                            document.get("name").toString(),
                            document.get("lat").toString().toDouble(),
                            document.get("lon").toString().toDouble(),
                            1
                        )
                    )
                }
                binding.rvFonts.setHasFixedSize(true)
                binding.rvFonts.layoutManager = LinearLayoutManager(context)
                context?.let { myAdapter.fontsAdminRecyclerAdapter(fonts, it) }
                binding.rvFonts.adapter = myAdapter
            }
        /*
        binding2.imageView2.setOnClickListener{
            db.collection("fonts").document("font1")
                .delete()
        }
        */

        ItemFontListAdmin()

        return binding.root
    }

}