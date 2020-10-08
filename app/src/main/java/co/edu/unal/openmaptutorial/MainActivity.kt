package co.edu.unal.openmaptutorial

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class MainActivity : AppCompatActivity() {
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private var mMapView: MapView? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //configuracion para asegurar espacio en cache antes de pedir permisos
        Configuration.getInstance()
            .load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext))

        //configuracion de la mapView
        setContentView(R.layout.activity_main)
        mMapView = findViewById<View>(R.id.map) as MapView
        mMapView!!.setTileSource(TileSourceFactory.MAPNIK)
        mMapView!!.setMultiTouchControls(true)

        val mapController = mMapView!!.controller
        mapController.setZoom(18.5)
        val startPoint = GeoPoint(4.6334, -74.0815)
        mapController.setCenter(startPoint)

        val mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(applicationContext), mMapView!!)
        mLocationOverlay.enableMyLocation()
        mMapView!!.overlays.add(mLocationOverlay)

        val items: ArrayList<OverlayItem> = ArrayList()
        items.add(OverlayItem("Universidad", "Universidad Nacional de Colombia",
            GeoPoint(4.6334, -74.0815 )))
        val mItemOverlay =
            ItemizedIconOverlay<OverlayItem>(
                items,
                object : OnItemGestureListener<OverlayItem?> {
                    override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean {
                        Toast.makeText(applicationContext, item?.title, Toast.LENGTH_LONG).show()
                        return true
                    }

                    override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean {
                        Toast.makeText(applicationContext, item?.snippet, Toast.LENGTH_LONG).show()
                        return true
                    }
                }, applicationContext)
        mMapView!!.overlays.add(mItemOverlay)

        requestPermissionsIfNecessary(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }

    public override fun onResume() {
        super.onResume()
        mMapView!!.onResume()
    }

    public override fun onPause() {
        super.onPause()
        mMapView!!.onPause()
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