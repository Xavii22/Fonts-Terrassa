package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.projecte.fonts_terrassa.ViewModel.ListViewModel
import cat.copernic.projecte.fonts_terrassa.ViewModel.UsersViewModel
import cat.copernic.projecte.fonts_terrassa.adapters.UsersRecyclerAdapter
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentUsersAdminListBinding
import cat.copernic.projecte.fonts_terrassa.models.User
import com.google.firebase.firestore.FirebaseFirestore

class UsersAdminListFragment : Fragment() {

    private lateinit var binding: FragmentUsersAdminListBinding
    private val myAdapter: UsersRecyclerAdapter = UsersRecyclerAdapter()
    private val ViewModel: UsersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_users_admin_list, container, false
        )

        ViewModel.getUsers(binding, requireContext())

        return binding.root
    }
}