package com.india.swachhbharat

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.india.dataset.Complain
import kotlinx.android.synthetic.main.activity_show_ditails.*
import org.json.JSONArray
import org.json.JSONObject

class ShowDitails : AppCompatActivity() , OnMapReadyCallback {

    lateinit var mMap: GoogleMap
    lateinit var dilog: Dialog
    lateinit var conpmail: JSONObject
    lateinit var mDatabase: DatabaseReference
    lateinit var mAuth: FirebaseAuth
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

        mDatabase = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()

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



        if(conpmail.getString("approve")=="true"){
            postimage.visibility = View.VISIBLE
            button.visibility =View.GONE
        }


        button.setOnClickListener {
            dilog.show()
            val currentUser = mAuth.currentUser
            writeNewUser(conpmail.getString("email"), conpmail.getString("des"),conpmail.getString("adress"), conpmail.getString("username"), java.lang.Double.parseDouble( conpmail.getString("Latitude")), java.lang.Double.parseDouble( conpmail.getString("Longitude")))
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun writeNewUser(emainid: String, des: String, adress: String, username: String?, Latitude: Double?, Longitude: Double?) {
        val user = Complain(des, adress, username, true, Latitude, Longitude)
//        val database = FirebaseDatabase.getInstance()
//        val key = database.getReference("quiz").push().key
        mDatabase.child("complains").child(emainid).child(conpmail.getString("id")).setValue(user).addOnSuccessListener {
            Log.e("sucess", "suess")
            Toast.makeText(this@ShowDitails, "Update Complain Successfully", Toast.LENGTH_SHORT).show()
            description.text = ""
            startActivity(Intent(this@ShowDitails,MainActivity::class.java))
            finish()
            dilog.dismiss()
        }
                .addOnFailureListener { e -> Log.e("sucess", e.toString()) }
    }
}
