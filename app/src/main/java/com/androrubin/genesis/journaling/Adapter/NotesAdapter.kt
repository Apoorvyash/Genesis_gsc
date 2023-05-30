package com.androrubin.genesis.journaling.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.androrubin.genesis.R
import com.androrubin.genesis.journaling.Model.Note

import java.util.Collections.list
import kotlin.random.Random

class NotesAdapter(private val context: Context, val listener:NotesItemClickListener):
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {


    private val NoteList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
      return NoteViewHolder(
          LayoutInflater.from(context).inflate(R.layout.item_journal,parent,false)
      )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = NoteList[position]
        holder.title.text = currentNote.title
        holder.title.isSelected=true
        holder.note.text = currentNote.note
        holder.date.text = currentNote.date
        holder.date.isSelected=true

        holder.notes_layout.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor(),null))

        holder.notes_layout.setOnClickListener {
            listener.onItemClicked(NoteList[holder.adapterPosition])
        }

        holder.notes_layout.setOnLongClickListener{
            listener.onLongItemClicked(NoteList[holder.adapterPosition],holder.notes_layout)
            true
        }
    }

    override fun getItemCount(): Int {

        return NoteList.size
    }

    fun updateList(newList : List<Note>)
    {

        fullList.clear()
        fullList.addAll(newList)

        NoteList.clear()
        NoteList.addAll(fullList)
        notifyDataSetChanged()
    }

    fun filterList(search : String){
        NoteList.clear()

        for(item in fullList){
            if(item.title?.lowercase()?.contains(search.lowercase())== true ||
                    item.note?.lowercase()?.contains(search.lowercase())==true){
                NoteList.add(item)
            }
        }
        notifyDataSetChanged()
    }

    fun randomColor():Int{
        val list = ArrayList<Int>()
        list.add(R.color.pastel_pink)
        list.add(R.color.pastel_blue)
        list.add(R.color.pastel_yellow)
        list.add(R.color.pastel_violet)
        list.add(R.color.pastel_green)
        list.add(R.color.pastel_orange)
        list.add(R.color.pastel_purple)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]

    }


    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val notes_layout = itemView.findViewById<CardView>(R.id.card1)
        val title = itemView.findViewById<TextView>(R.id.Title)
        val note = itemView.findViewById<TextView>(R.id.note)
        val date = itemView.findViewById<TextView>(R.id.date)
    }

    interface NotesItemClickListener{

        fun onItemClicked(note: Note)
        fun onLongItemClicked(note:Note,cardView: CardView)

}
    }