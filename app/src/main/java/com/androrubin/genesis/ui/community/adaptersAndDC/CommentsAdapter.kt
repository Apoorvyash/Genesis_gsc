package com.androrubin.genesis.ui.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androrubin.genesis.R
import com.androrubin.genesis.ui.community.adaptersAndDC.CommentsDC
import com.androrubin.genesis.ui.community.adaptersAndDC.CommunityDC

class CommentsAdapter(private val commentsList : ArrayList<CommentsDC>) : RecyclerView.Adapter<CommentsAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.comments_element,
            parent,false)

        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = commentsList[position]
        holder.posterName.text = currentItem.commentorName
        holder.description.text = currentItem.comment
    }


    override fun getItemCount(): Int
    {
        return commentsList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val posterName : TextView = itemView.findViewById(R.id.commentorNameTV)
        val description : TextView = itemView.findViewById(R.id.commentTV)
    }
}