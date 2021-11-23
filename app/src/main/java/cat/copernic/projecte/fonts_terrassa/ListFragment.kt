
package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ListFragment : Fragment() {
<<<<<<< Updated upstream
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
=======

    private lateinit var binding: FragmentListBinding
    private val myAdapter: FontRecyclerAdapter = FontRecyclerAdapter()
    private val db = FirebaseFirestore.getInstance()
    private var fonts: ArrayList<Font> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list, container, false
        )

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
                context?.let { myAdapter.fontsRecyclerAdapter(fonts, it) }
                binding.rvFonts.adapter = myAdapter
            }

        return binding.root
>>>>>>> Stashed changes
    }
}