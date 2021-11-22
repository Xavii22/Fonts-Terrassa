package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.projecte.fonts_terrassa.adapters.UsersRecyclerAdapter
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentListBinding
import cat.copernic.projecte.fonts_terrassa.models.Font
import cat.copernic.projecte.fonts_terrassa.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class usersAdminListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private val myAdapter: UsersRecyclerAdapter = UsersRecyclerAdapter()
    private val db = FirebaseFirestore.getInstance()
    private var users: ArrayList<User> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_users_admin_list, container, false)


        db.collection("users")
            .get()
            .addOnSuccessListener{ documents ->
                for (document in documents){
                    users.add(
                        User(
                        document.get("email").toString())
                    )
                }
                binding.rvFonts.setHasFixedSize(true)
                binding.rvFonts.layoutManager = LinearLayoutManager(context)
                context?.let { myAdapter.UsersRecyclerAdapter(users, it) }
                binding.rvFonts.adapter = myAdapter
            }

        return binding.root
    }
}