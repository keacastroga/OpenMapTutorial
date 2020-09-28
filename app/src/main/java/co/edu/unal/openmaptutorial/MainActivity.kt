package co.edu.unal.openmaptutorial

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.*


class MainActivity : AppCompatActivity() {
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private var nMapView: MapView? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //configuracion para asegurar espacio en cache antes de pedir permisos
        Configuration.getInstance()
            .load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext))

        //configuracion de la mapView
        setContentView(R.layout.activity_main)
        nMapView = findViewById<View>(R.id.map) as MapView
        nMapView!!.setTileSource(TileSourceFactory.MAPNIK)
        nMapView!!.setMultiTouchControls(true)

        val mapController = nMapView!!.controller
        mapController.setZoom(18.5)
        val startPoint = GeoPoint(4.6334, -74.0815)
        mapController.setCenter(startPoint)

        val mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(applicationContext), nMapView!!)
        mLocationOverlay.enableMyLocation()
        nMapView!!.overlays.add(mLocationOverlay)

        requestPermissionsIfNecessary(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )

    }

    public override fun onResume() {
        super.onResume()
        nMapView!!.onResume()
    }

    public override fun onPause() {
        super.onPause()
        nMapView!!.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        val permissionsToRequest =
            ArrayList<String>()
        for (i in grantResults.indices) {
            permissionsToRequest.add(permissions[i])
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    private fun requestPermissionsIfNecessary(permissions: Array<String>) {
        val permissionsToRequest =
            ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted
                permissionsToRequest.add(permission)
            }
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }
}