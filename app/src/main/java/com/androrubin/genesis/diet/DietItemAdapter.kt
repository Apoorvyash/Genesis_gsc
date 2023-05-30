package com.androrubin.genesis.diet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androrubin.genesis.R

class DietItemAdapter (private val dietList: ArrayList<DietItem>) :

    RecyclerView.Adapter<DietItemAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.diet_item,
            parent,false)
        return ViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentitem = dietList[position]
        holder.itemName.text = currentitem.item
        holder.itemQnt.text = currentitem.qnt.toString()
    }

    override fun getItemCount(): Int {
        return dietList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val itemName = itemView.findViewById<TextView>(R.id.item)
        val itemQnt = itemView.findViewById<TextView>(R.id.item_qn)



    }
}