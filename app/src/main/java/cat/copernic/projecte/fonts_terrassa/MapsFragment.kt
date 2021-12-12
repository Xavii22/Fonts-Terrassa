package cat.copernic.projecte.fonts_terrassa
import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import cat.copernic.projecte.fonts_terrassa.databinding.FragmentMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore

class MapsFragment : Fragment(), OnMapReadyCallback {

    private val db= FirebaseFirestore.getInstance()
    private lateinit var binding: FragmentMapsBinding
    private var mapType = 0
    var fontsArray = arrayOf(
        "Fonts de beure", "Fonts de beure singulars", "Fonts ornamentals",
        "Font naturals", "Fonts de gossos"
    )
    lateinit var selectedFont: BooleanArray

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val callback = OnMapReadyCallback { googleMap ->

        onMapReady(googleMap)
        selectedFont = BooleanArray(fontsArray.size)

        for (j in 0..4) {
            selectedFont[j] = true
        }

        loadMap(googleMap)
        val mapPos = LatLng(41.56321391021604, 2.010025253878396)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(mapPos))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(13.5f))
        googleMap.setOnInfoWindowClickListener {
            var fontId = ""
            db.collection("fonts")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if(document.get("name") == it.title){
                            fontId = document.get("id").toString()
                        }
                }
                    val bundle = Bundle()
                    bundle.putSerializable("fontId", fontId)
                    findNavController().navigate(R.id.action_mapsFragment_to_viewFontFragment, bundle)
            }
        }

        binding.btnFilter.setOnClickListener{

            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Seleccionar tipus de font")

            googleMap.clear()

            //ClearFontsByType
            builder.setCancelable(false)
            builder.setMultiChoiceItems(
                fontsArray, selectedFont
            ) { _, _, _ ->

            }
            builder.setPositiveButton(
                "Acceptar"
            ) { dialogInterface, i ->
                loadMap(googleMap)
            }
            builder.show()
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
        savedInstanceState: Bundle?,
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

    fun loadMap(googleMap: GoogleMap){
        if(selectedFont[0]) {
            db.collection("fonts").whereEqualTo("type", 1).get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val pos = LatLng(
                        document.get("lat").toString().toDouble(),
                        document.get("lon").toString().toDouble()
                    )
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(pos)
                            .title(document.get("name").toString())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.gota_1))
                    )
                }
            }
        }
        if(selectedFont[1]) {
            db.collection("fonts").whereEqualTo("type", 2).get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val pos = LatLng(
                        document.get("lat").toString().toDouble(),
                        document.get("lon").toString().toDouble()
                    )
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(pos)
                            .title(document.get("name").toString())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.gota_2))
                    )
                }
            }
        }
        if(selectedFont[2]) {
            db.collection("fonts").whereEqualTo("type", 3).get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val pos = LatLng(
                        document.get("lat").toString().toDouble(),
                        document.get("lon").toString().toDouble()
                    )
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(pos)
                            .title(document.get("name").toString())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.gota_3))
                    )
                }
            }
        }
        if(selectedFont[3]) {
            db.collection("fonts").whereEqualTo("type", 4).get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val pos = LatLng(
                        document.get("lat").toString().toDouble(),
                        document.get("lon").toString().toDouble()
                    )
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(pos)
                            .title(document.get("name").toString())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.gota_4))
                    )
                }
            }
        }
        if(selectedFont[4]) {
            db.collection("fonts").whereEqualTo("type", 5).get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val pos = LatLng(
                        document.get("lat").toString().toDouble(),
                        document.get("lon").toString().toDouble()
                    )
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(pos)
                            .title(document.get("name").toString())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.gota_5))
                    )
                }
            }
        }
    }

    override fun onMapReady(myMap: GoogleMap) {
        if (context?.let {
                ActivityCompat.checkSelfPermission(it,
                    Manifest.permission.ACCESS_FINE_LOCATION)
            } != PackageManager.PERMISSION_GRANTED && context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
            } != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        myMap.isMyLocationEnabled = true
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                val mapPos = location?.let { LatLng(it.latitude, location.longitude) }
                mapPos?.let { CameraUpdateFactory.newLatLng(it) }?.let { myMap.moveCamera(it) }
                myMap.moveCamera(CameraUpdateFactory.zoomTo(13.5f))
            }
    }
}
