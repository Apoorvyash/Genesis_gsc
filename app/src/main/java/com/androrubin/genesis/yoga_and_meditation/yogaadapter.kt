package com.apoorv.runanddetect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androrubin.genesis.R
import com.androrubin.genesis.yoga_and_meditation.OnClickItemListener

class yogaadapter(private val listMembers: ArrayList<yogacModel>, private val onClickItemListener: OnClickItemListener) : RecyclerView.Adapter<yogaadapter.ViewHolder>() {


        class ViewHolder(private val item: View): RecyclerView.ViewHolder(item) {
            val yoganame: TextView = item.findViewById<TextView>(R.id.yoga_name)
            val yogaDetails: TextView=item.findViewById<TextView>(R.id.yoga_details)
            val yogaImg: ImageView=item.findViewById<ImageView>(R.id.yoga_image)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater=LayoutInflater.from(parent.context)
            val item= inflater.inflate(R.layout.recycler_item, parent, false)
            return ViewHolder(item)
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item=listMembers[position]
//            holder.yogaImg.imageAlpha=item.y
            holder.yoganame.text=item.yogaName
            holder.yogaDetails.text=item.yogaDetails
            val YogaImage=item.imgId
            holder.yogaImg.setImageResource(YogaImage)
            holder.itemView.setOnClickListener{
                onClickItemListener.onMovieItemClicked(position)
            }

        }
        override fun getItemCount(): Int {
            return listMembers.size
        }
    }