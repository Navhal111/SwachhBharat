package com.india.adapter


import android.content.Context
import android.content.Intent
import android.view.*

import android.view.ViewGroup
import android.content.SharedPreferences
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.india.swachhbharat.R
import com.india.swachhbharat.ShowDitails
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import org.json.JSONArray


class Complain (var name: JSONArray): RecyclerView.Adapter<Complain.ViewHolder>() {

    lateinit var context1:Context

    lateinit var sharedads: SharedPreferences
    private val DECCELERATE_INTERPOLATOR = DecelerateInterpolator()
    private val ACCELERATE_INTERPOLATOR = AccelerateInterpolator()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var jsonObject =  name.getJSONObject(position)

        holder.post_username.text = jsonObject.getString("des")

        holder.post_timestamp.text = jsonObject.getString("username")

        holder.card_image.setOnClickListener {

            var intent  = Intent(context1,ShowDitails::class.java)
            intent.putExtra("json",jsonObject.toString())
            context1.startActivity(intent)

        }

        if(jsonObject.getString("approve") == "true"){
            holder.postimage.visibility = View.VISIBLE
        }else{
            holder.postimage.visibility = View.GONE
        }
    }


    override fun getItemCount(): Int {
        return name.length()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Complain.ViewHolder {

        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        context1=parent.context
        return Complain.ViewHolder(itemView)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var post_username:TextView = itemView.find(R.id.post_username)
        var post_timestamp:TextView = itemView.find(R.id.post_timestamp)
        var card_image:CardView =itemView.find(R.id.card_image)
        var postimage:TextView =itemView.find(R.id.postimage)

        init {

        }
    }

}