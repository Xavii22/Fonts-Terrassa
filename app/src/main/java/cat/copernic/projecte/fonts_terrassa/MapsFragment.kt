package cat.copernic.projecte.fonts_terrassa
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore

class MapsFragment : Fragment() {

    private val db= FirebaseFirestore.getInstance()
    private lateinit var binding: FragmentMapsBinding
    private var mapType = 0

    private val callback = OnMapReadyCallback { googleMap ->

        db.collection("fonts").whereEqualTo("type", 1).get().addOnSuccessListener{ documents ->
            for (document in documents) {
                val pos = LatLng(document.get("lat").toString().toDouble(), document.get("lon").toString().toDouble())
                googleMap.addMarker(
                    MarkerOptions()
                        .position(pos)
                        .title(document.get("name").toString())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_font))
                )
            }
        }

        val mapPos = LatLng(41.56321391021604, 2.010025253878396)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(mapPos))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(13.5f))
        googleMap.setOnInfoWindowClickListener {
            findNavController().navigate(MapsFragmentDirections.actionMapsFragmentToViewFontFragment())
        }

        binding.btnChangeMap.setOnClickListener{
            if(mapType == 0){
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE)
                mapType = 1
            }else{
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)
                mapType = 0
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_maps, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}
