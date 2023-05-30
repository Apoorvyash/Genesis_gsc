package com.androrubin.genesis.ui.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androrubin.genesis.R
import com.androrubin.genesis.ui.community.adaptersAndDC.CommunityDC

class CommunityAdapter(private val postList : ArrayList<CommunityDC>) : RecyclerView.Adapter<CommunityAdapter.MyViewHolder>(){

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position : Int)

    }

    fun setOnItemClickListener(listener: onItemClickListener){

        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.community_post_item1,
            parent,false)

        return MyViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = postList[position]
        holder.posterName.text = currentItem.posterName
        holder.description.text = currentItem.description
        holder.date.text = currentItem.date
        holder.upvotes.text = currentItem.upvotes
        holder.comments.text = currentItem.comments

    }

    override fun getItemCount(): Int
    {
        return postList.size
    }

    class MyViewHolder(itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val posterName : TextView = itemView.findViewById(R.id.posterNameEdt)
        val description : TextView = itemView.findViewById(R.id.descriptionEdt)
        val date :TextView = itemView.findViewById(R.id.dateOrTimeEdt)
        val comments : TextView = itemView.findViewById(R.id.commentsEdt)
        val upvotes : TextView = itemView.findViewById(R.id.upvotesEdt)

        init {

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}