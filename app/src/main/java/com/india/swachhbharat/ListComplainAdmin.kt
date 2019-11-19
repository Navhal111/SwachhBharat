package com.india.swachhbharat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.india.adapter.Complain
import kotlinx.android.synthetic.main.activity_list_complain.*
import org.json.JSONArray
import org.json.JSONObject

class ListComplainAdmin : AppCompatActivity() {
    lateinit var conpmail: JSONArray
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_complain)

        supportActionBar!!.title = "List Complaint"
        mAuth = FirebaseAuth.getInstance()


        val currentUser = mAuth!!.getCurrentUser()
        val database = FirebaseDatabase.getInstance().reference

        posts_recyclerview.layoutManager = LinearLayoutManager(this)
        conpmail = JSONArray()
        posts_recyclerview.adapter  =  Complain(conpmail)
        database.child("complains").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (noteDataSnapshot in dataSnapshot.children) {

                    database.child("complains").child(noteDataSnapshot.key.toString()).addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot1: DataSnapshot) {

                            for (noteDataSnapshot1 in dataSnapshot1.children) {
                                var jsonObject = JSONObject()
                                jsonObject.put("id", noteDataSnapshot1.key)
                                jsonObject.put("email",noteDataSnapshot.key)
                                jsonObject.put("username", noteDataSnapshot1.child("username").value.toString())
                                jsonObject.put("des", noteDataSnapshot1.child("des").value.toString())
                                jsonObject.put("approve", noteDataSnapshot1.child("approve").value.toString())
                                jsonObject.put("adress", noteDataSnapshot1.child("adress").value.toString())
                                jsonObject.put("Latitude", noteDataSnapshot1.child("Latitude").value.toString())
                                jsonObject.put("Longitude", noteDataSnapshot1.child("Longitude").value.toString())
                                conpmail.put(jsonObject)
                            }

                            posts_recyclerview.adapter!!.notifyDataSetChanged()
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.e("error",databaseError.message)

                        }

                    })

                }

                Log.e("seeter",  conpmail.toString())

                posts_recyclerview.adapter!!.notifyDataSetChanged()
                progress.visibility = View.GONE

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("error",databaseError.message)
            }

        })

        refreshLayout.setOnRefreshListener {
            conpmail = JSONArray()
            posts_recyclerview.adapter  =  Complain(conpmail)
            database.child("complains").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (noteDataSnapshot in dataSnapshot.children) {

                        database.child("complains").child(noteDataSnapshot.key.toString()).addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot1: DataSnapshot) {

                                for (noteDataSnapshot1 in dataSnapshot1.children) {
                                    var jsonObject = JSONObject()
                                    jsonObject.put("id", noteDataSnapshot1.key)
                                    jsonObject.put("email",noteDataSnapshot.key)
                                    jsonObject.put("username", noteDataSnapshot1.child("username").value.toString())
                                    jsonObject.put("des", noteDataSnapshot1.child("des").value.toString())
                                    jsonObject.put("approve", noteDataSnapshot1.child("approve").value.toString())
                                    jsonObject.put("adress", noteDataSnapshot1.child("adress").value.toString())
                                    jsonObject.put("Latitude", noteDataSnapshot1.child("Latitude").value.toString())
                                    jsonObject.put("Longitude", noteDataSnapshot1.child("Longitude").value.toString())
                                    conpmail.put(jsonObject)
                                }

                                posts_recyclerview.adapter!!.notifyDataSetChanged()
                                refreshLayout.isRefreshing =false
                            }
                            override fun onCancelled(databaseError: DatabaseError) {
                                Log.e("error",databaseError.message)
                            }

                        })

                    }

                    Log.e("seeter",  conpmail.toString())

                    posts_recyclerview.adapter!!
                            .notifyDataSetChanged()
                    progress.visibility = View.GONE

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("error",databaseError.message)
                }

            })
        }
    }


    override fun onBackPressed() {
        finish()
    }


}
