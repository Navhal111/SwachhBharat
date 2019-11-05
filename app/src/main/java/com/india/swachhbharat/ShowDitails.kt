package com.india.swachhbharat

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_show_ditails.*
import org.json.JSONArray
import org.json.JSONObject

class ShowDitails : AppCompatActivity() , OnMapReadyCallback {

    lateinit var mMap: GoogleMap
    lateinit var dilog: Dialog
    lateinit var conpmail: JSONObject

    override fun onMapReady(p0: GoogleMap?) {


        mMap = p0!!

        val sydney = LatLng(java.lang.Double.parseDouble( conpmail.getString("Latitude")), java.lang.Double.parseDouble( conpmail.getString("Longitude")))
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker your Location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14.0f))
        dilog.dismiss()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_ditails)

        supportActionBar!!.title = "Complaint Details"

        conpmail = JSONObject(intent.getStringExtra("json"))

        post_username.text =  "UserName:- "+ conpmail.getString("username")
        description.text =  "Description:- "+ conpmail.getString("des")
        adress.text =  "Landmark:- "+conpmail.getString("adress")

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        dilog = Dialog(this)
        dilog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dilog.setCancelable(false)
        dilog.setContentView(R.layout.dialog)
        dilog.show()


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
