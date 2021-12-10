
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

        //Valorar Gust
        var gustValue = 0
        binding.starGustVal1.setOnClickListener{
            gustValue = 1
            binding.starGustVal1.setImageResource(R.drawable.ic_starcomplete)
            binding.starGustVal2.setImageResource(R.drawable.ic_starempty)
            binding.starGustVal3.setImageResource(R.drawable.ic_starempty)
            binding.starGustVal4.setImageResource(R.drawable.ic_starempty)
            binding.starGustVal5.setImageResource(R.drawable.ic_starempty)
        }
        binding.starGustVal2.setOnClickListener{
            gustValue = 2
            binding.starGustVal1.setImageResource(R.drawable.ic_starcomplete)
            binding.starGustVal2.setImageResource(R.drawable.ic_starcomplete)
            binding.starGustVal3.setImageResource(R.drawable.ic_starempty)
            binding.starGustVal4.setImageResource(R.drawable.ic_starempty)
            binding.starGustVal5.setImageResource(R.drawable.ic_starempty)
        }
        binding.starGustVal3.setOnClickListener{
            gustValue = 3
            binding.starGustVal1.setImageResource(R.drawable.ic_starcomplete)
            binding.starGustVal2.setImageResource(R.drawable.ic_starcomplete)
            binding.starGustVal3.setImageResource(R.drawable.ic_starcomplete)
            binding.starGustVal4.setImageResource(R.drawable.ic_starempty)
            binding.starGustVal5.setImageResource(R.drawable.ic_starempty)
        }
        binding.starGustVal4.setOnClickListener{
            gustValue = 4
            binding.starGustVal1.setImageResource(R.drawable.ic_starcomplete)
            binding.starGustVal2.setImageResource(R.drawable.ic_starcomplete)
            binding.starGustVal3.setImageResource(R.drawable.ic_starcomplete)
            binding.starGustVal4.setImageResource(R.drawable.ic_starcomplete)
            binding.starGustVal5.setImageResource(R.drawable.ic_starempty)
        }
        binding.starGustVal5.setOnClickListener{
            gustValue = 5
            binding.starGustVal1.setImageResource(R.drawable.ic_starcomplete)
            binding.starGustVal2.setImageResource(R.drawable.ic_starcomplete)
            binding.starGustVal3.setImageResource(R.drawable.ic_starcomplete)
            binding.starGustVal4.setImageResource(R.drawable.ic_starcomplete)
            binding.starGustVal5.setImageResource(R.drawable.ic_starcomplete)
        }

        binding.btnEvaluate.setOnClickListener {
            if (gustValue != 0) {
                binding.spinnerOlor.selectedItem.toString()
                binding.spinnerTransperencia.selectedItem.toString()
                //Send values to DB
                db.collection("valoracions").document()
                    .set(
                        hashMapOf(
                            "font" to fontId,
                            "gust" to gustValue,
                            "olor" to binding.spinnerOlor.selectedItem.toString().toInt(),
                            "transperencia" to binding.spinnerTransperencia.selectedItem.toString()
                                .toInt()
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
                val bundle = Bundle()
                bundle.putSerializable("fontId", fontId)
                findNavController().navigate(R.id.action_evaluateFragment_to_viewFontFragment,
                    bundle)
            }else{
                //Show Snackbar
                view?.let { it1 ->
                    Snackbar.make(
                        it1,
                        "No s'han evaluat tots els camps",
                        BaseTransientBottomBar.LENGTH_SHORT
                    ).show()
                }
            }
        }

        return binding.root
    }
}