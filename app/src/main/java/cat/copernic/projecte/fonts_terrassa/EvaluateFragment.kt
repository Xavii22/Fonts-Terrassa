
package cat.copernic.projecte.fonts_terrassa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentEvaluateBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class EvaluateFragment : Fragment() {
    private val db= FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentEvaluateBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_evaluate, container, false)

        val fontId = arguments?.getString("fontId")

        binding.btnEvaluate.setOnClickListener{
            binding.spinnerGust.selectedItem.toString()
            binding.spinnerOlor.selectedItem.toString()
            binding.spinnerTransperencia.selectedItem.toString()
            //Send values to DB
            db.collection("valoracions").document()
                .set(
                    hashMapOf(
                        "font" to fontId,
                        "gust" to binding.spinnerGust.selectedItem.toString().toInt(),
                        "olor" to binding.spinnerOlor.selectedItem.toString().toInt(),
                        "transperencia" to binding.spinnerTransperencia.selectedItem.toString().toInt()
                    )
                )
            //Show Snackbar
            view?.let { it1 ->
                Snackbar.make(
                    it1,
                    "Gràcies per avaular la font",
                    BaseTransientBottomBar.LENGTH_SHORT
                ).show()
            }

            //GO to viewFont
            findNavController().navigate(EvaluateFragmentDirections.actionEvaluateFragmentToViewFontFragment())
        }

        return binding.root
    }
}