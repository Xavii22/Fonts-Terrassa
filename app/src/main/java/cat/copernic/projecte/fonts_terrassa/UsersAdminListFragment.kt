package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.projecte.fonts_terrassa.adapters.UsersRecyclerAdapter
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentUsersAdminListBinding
import cat.copernic.projecte.fonts_terrassa.models.User
import com.google.firebase.firestore.FirebaseFirestore

class UsersAdminListFragment : Fragment() {

    private lateinit var binding: FragmentUsersAdminListBinding
    private val myAdapter: UsersRecyclerAdapter = UsersRecyclerAdapter()
    private val db = FirebaseFirestore.getInstance()
    private var users: ArrayList<User> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_users_admin_list, container, false
        )


        db.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    users.add(
                        User(
                            document.get("email").toString()
                        )
                    )
                }
                binding.rvUsers.setHasFixedSize(true)
                binding.rvUsers.layoutManager = LinearLayoutManager(context)
                context?.let { myAdapter.UsersRecyclerAdapter(users, it) }
                binding.rvUsers.adapter = myAdapter
            }

        return binding.root
    }
}