package com.androrubin.genesis.journaling.recycler

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.androrubin.genesis.R
import java.util.*

class JournelAdapter(val context: Context, private val journelList: ArrayList<JournelEle>): RecyclerView.Adapter<JournelAdapter.MyViewHolder>()  {

    var rand: Random = Random()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.journal_item, parent, false)
        val cardV = itemView.findViewById<CardView>(R.id.card1)
        val currentColor: Int =
            Color.argb(255, rand.nextInt(200), rand.nextInt(200), rand.nextInt(220))
        cardV.setCardBackgroundColor(currentColor)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = journelList[position]
        holder.title.text = currentItem.title
        holder.desc.text = currentItem.desc
        holder.date.text = currentItem.date
          }

    override fun getItemCount(): Int {
        return journelList.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.Title)
        val desc = itemView.findViewById<TextView>(R.id.content)
        val date = itemView.findViewById<TextView>(R.id.date)
    }
}