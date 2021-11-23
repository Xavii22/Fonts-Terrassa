package cat.copernic.projecte.fonts_terrassa

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.copernic.projecte.fonts_terrassa.databinding.ItemFontListAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class ItemFontListAdmin : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: ItemFontListAdminBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_font_admin_list, container, false)

        binding.imageView2.setOnClickListener {
            db.collection("fonts").document("fontProva")
                .delete()
                .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error deleting document", e) }
        }

        return binding.root
    }
}
