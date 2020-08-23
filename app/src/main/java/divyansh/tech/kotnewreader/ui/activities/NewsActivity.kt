package divyansh.tech.kotnewreader.ui.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.models.User
import divyansh.tech.kotnewreader.ui.viewModels.newsViewModel
import kotlinx.android.synthetic.main.activity_news.*
import java.util.*

@AndroidEntryPoint
class NewsActivity : AppCompatActivity(),LocationListener {

    val viewModel: newsViewModel by viewModels()
    lateinit var user: User
    var country: String? = null
    var city: String? = null
    private lateinit var mLocationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
        viewModel.newRepository.testIfInjected()
        initUser()
    }

    override fun onResume() {
        super.onResume()
        fetchLocation()
    }

    private fun fetchLocation() {
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            buildAlertMessage()
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
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
            mLocationManager.let {
                it.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 10f, this)
                val loc = it.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                Log.i("MainActivity", "In location manager " + loc?.toString())
                loc?.let {
                    getGeoAddress(it.latitude, it.longitude)
                }
            }
        }
    }

    private fun buildAlertMessage() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.locationPermission))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)
            ) { _, _ -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton(getString(R.string.no)
            ) { dialog, _ -> dialog?.cancel() }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun getGeoAddress(lat: Double, long: Double) {
        val address = Geocoder(this, Locale.getDefault()).getFromLocation(lat, long, 1)
        country = address.get(0).countryCode.toLowerCase()
        city = address.get(0).locality
        Log.i("MainActivity", country + " " + city)
    }

    override fun onRequestPermissionsResult(requestCode:Int,
                                            permissions:Array<String>,
                                            grantResults:IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fetchLocation()
    }

    private fun initUser() {
        user = intent.getSerializableExtra(getString(R.string.userArgument)) as User
    }

    override fun onLocationChanged(location: Location?) {
        Log.i("MainActivity", location.toString())
//        getGeoAddress(location.latitude, location.longitude)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
//        TODO("Not yet implemented")
    }

    override fun onProviderEnabled(provider: String?) {
        Log.i("MainActivity", "enabled")
        fetchLocation()
    }

    override fun onProviderDisabled(provider: String?) {
        Log.i("MainActivity", "disabled")
    }
}