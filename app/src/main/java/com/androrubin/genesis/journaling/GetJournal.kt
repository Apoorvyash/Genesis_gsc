package com.androrubin.genesis.journaling

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androrubin.genesis.R
import com.androrubin.genesis.databinding.ActivityGetJournalBinding
import com.androrubin.genesis.journaling.Adapter.NotesAdapter
import com.androrubin.genesis.journaling.Database.NoteDatabase
import com.androrubin.genesis.journaling.Model.Note
import com.androrubin.genesis.journaling.Model.NoteViewModel
import com.androrubin.genesis.journaling.recycler.JournelAdapter
import com.androrubin.genesis.journaling.recycler.JournelEle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GetJournal : AppCompatActivity(), NotesAdapter.NotesItemClickListener,PopupMenu.OnMenuItemClickListener  {


    private  lateinit var auth : FirebaseAuth
    private lateinit var journalRecyclerView: RecyclerView
    private lateinit var journalList:ArrayList<JournelEle>
    private lateinit var adapter1: JournelAdapter
    private lateinit var mDbRef: FirebaseFirestore

    private lateinit var binding:ActivityGetJournalBinding
    private lateinit var database: NoteDatabase
    lateinit var viewModel: NoteViewModel
    lateinit var adapter: NotesAdapter
    lateinit var selectedNote : Note

    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->

        if(result.resultCode == Activity.RESULT_OK){

            val note = result.data?.getSerializableExtra("note")as? Note
            if(note != null){

                viewModel.updateNote(note)
            }

        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGetJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        viewModel = ViewModelProvider(this,ViewModelProvider
            .AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)

        viewModel.allnotes.observe(this){ list ->
            list.let{
                adapter.updateList(list)
            }

        }

        database = NoteDatabase.getDatabase(this)

    }





    private  fun initUI(){
        binding.journalRecyclerview.setHasFixedSize(true)
        binding.journalRecyclerview.layoutManager = LinearLayoutManager(this)
        adapter = NotesAdapter(this,this)
        binding.journalRecyclerview.adapter = adapter


        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

            if(result.resultCode == Activity.RESULT_OK){

                val note = result.data?.getSerializableExtra("note")as? Note
                if(note!= null){

                    viewModel.insertNote(note)
                }
            }

        }

        binding.calendar.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        binding.writeJournal.setOnClickListener {
            val intent = Intent(this, RecordJournalActivity::class.java)
            getContent.launch(intent)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }




            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null){

                    adapter.filterList(newText)
                }
                return true
            }
        })

    }

    override fun onItemClicked(note: Note) {

        val intent = Intent(this, RecordJournalActivity:: class.java)
        intent.putExtra("current_note",note)
        updateNote.launch(intent)
    }

    override fun onLongItemClicked(note: Note, cardView: CardView) {

        selectedNote = note
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView){
        val popup = PopupMenu(this,cardView)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()
    }

    override  fun onMenuItemClick(item: MenuItem?):Boolean{

        if(item?.itemId == R.id.delete_entry){
            viewModel.deleteNote(selectedNote)
            return true

        }
        return  false
    }


}