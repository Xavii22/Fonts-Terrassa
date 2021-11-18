package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

       binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list, container, false)

        setUpRecyclerView()

        return binding.root
    }

    private fun setUpRecyclerView(){
        //Especifiquem que els fills del RV seran del mateix tamany i així optimitzem la seva creació
        binding.rvFonts.setHasFixedSize(true)

        //indiquem que el RV es mostrarà en format llista
        binding.rvFonts.layoutManager = LinearLayoutManager(context)

        //generem el adapter
        context?.let { myAdapter.fontsRecyclerAdapter(getFonts(), it) }

        //assignem el adapter al RV
        binding.rvFonts.adapter = myAdapter
    }

    private fun getFonts(): MutableList<Font> {
        val fonts: MutableList<Font> = arrayListOf()
        db.collection("fonts").whereEqualTo("type", 1).get().addOnSuccessListener{ documents ->
            for (document in documents) {
                fonts.add(Font(document.get("name").toString(), document.get("lat").toString().toDouble(),
                    document.get("lon").toString().toDouble(), 1))
            }
        }

        return fonts
    }
}