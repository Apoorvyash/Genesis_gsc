package com.androrubin.genesis.ui.aid.book_caretaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androrubin.genesis.R
import com.androrubin.genesis.ui.aid.book_appointment.AvailableDoctorsDC

class AvailableCaretakersAdapter(private val commentsList : ArrayList<AvailableCareTakersDC>) : RecyclerView.Adapter<AvailableCaretakersAdapter.MyViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position : Int)

    }

    fun setOnItemClickListener(listener: onItemClickListener){

        mListener = listener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.available_doctors_element_list,
            parent, false
        )

        return MyViewHolder(itemView,mListener)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = commentsList[position]
        holder.doctorName.text = currentItem.doctorName
        holder.yoe.text = currentItem.yoe
        holder.specialization.text = currentItem.specialization

    }


    override fun getItemCount(): Int {
        return commentsList.size
    }

    class MyViewHolder(itemView: View, listener: AvailableCaretakersAdapter.onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val doctorName: TextView = itemView.findViewById(R.id.doctorNameTv)
        val yoe: TextView = itemView.findViewById(R.id.experienceYearsTv)
        val specialization: TextView = itemView.findViewById(R.id.specializationTv)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}