
package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentViewFontBinding
import android.content.Intent
import android.net.Uri
import android.util.Log
import cat.copernic.projecte.fonts_terrassa.databinding.ItemUserListBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_edit_font.*


class ItemUserList : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: ItemUserListBinding = DataBindingUtil.inflate(
            inflater, R.layout.item_user_list, container, false)

        binding.btnDelete.setOnClickListener{
            db.collection("users").document(binding.txtUser.text.toString()).delete()
        }

        return binding.root
    }
}
