package com.androrubin.genesis.journaling

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.androrubin.genesis.databinding.ActivityRecordJournalBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*





class RecordJournalActivity : AppCompatActivity() {

    private lateinit var db:FirebaseFirestore

    private lateinit var binding: ActivityRecordJournalBinding
    lateinit var current_note: com.androrubin.genesis.journaling.Model.Note
    lateinit var old_note : com.androrubin.genesis.journaling.Model.Note
    var isUpdate = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRecordJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val formatter = SimpleDateFormat("EEE,d MMM yyyy ")
        binding.date.setText((formatter.format(Date())).toString())

        try{

            old_note = intent.getSerializableExtra("current_note")as com.androrubin.genesis.journaling.Model.Note
            binding.Title.setText(old_note.title)
            binding.note.setText(old_note.note)
            binding.date.setText(old_note.date)
            isUpdate = true
        }catch (e:Exception){
            e.printStackTrace()
        }

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }


        binding.saveBtn.setOnClickListener {


            val title = binding.Title.text.toString()
            val note = binding.note.text.toString()


            if(title.isNotEmpty( ) || note.isNotEmpty()){



                if(isUpdate){

                    current_note = com.androrubin.genesis.journaling.Model.Note(
                        old_note.id,
                        title,
                        note,
                        formatter.format(Date())
                    )
                } else{

                    current_note= com.androrubin.genesis.journaling.Model.Note(
                        null,
                        title,
                        note,
                        formatter.format(Date())
                    )
                }

                val intent = Intent()
                intent.putExtra("note",current_note)
                setResult(Activity.RESULT_OK,intent)
                finish()

            }
            else{

                Toast.makeText(this,"Please enter some data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }




        }































//        db= FirebaseFirestore.getInstance()
//
//        val title= findViewById<EditText>(R.id.Title)
//        val des = findViewById<EditText>(R.id.note)
//        val calendar = Calendar.getInstance().time
//        val dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(calendar)
//        binding.date.text= dateFormat


        //var name = intent.getStringExtra("date+month")

//        val savebtn= findViewById<TextView>(R.id.save_btn)
//        savebtn.setOnClickListener {
//            startActivity(Intent(this,HomeActivity::class.java))
//        }


//        savebtn?.setOnClickListener {
//
////            Toast.makeText(this,title.getText().toString(),Toast.LENGTH_SHORT).show()
//            if(TextUtils.isEmpty(title.getText().trim().toString())) {
//                title.setError("Title cannot be empty")
//                title.requestFocus()
//            }else if (TextUtils.isEmpty(des.getText().trim().toString())){
//                des.setError("Journal cannot be empty")
//                des.requestFocus()
//            }
//            else{
//                val data = hashMapOf(
//                    "title" to title.getText().toString(),
//                    "description" to des.getText().toString(),
//                    "date" to binding.date.text.toString(),
//                    "time" to FieldValue.serverTimestamp()
//                )
//                db.collection("$dateFormat")
//                    .add(data)
//                    .addOnSuccessListener { documentReference ->
//                        Log.d("Data Addition", "DocumentSnapshot written with ID: ${documentReference.id}")
//                    }
//                    .addOnFailureListener { e ->
//                        Log.w("Data Addition", "Error adding document", e)
//                    }
//                val intent = Intent(this,HomeActivity::class.java)
//                Toast.makeText(this,"Post Added",Toast.LENGTH_SHORT).show()
//                startActivity(intent)
//                finishAffinity()
//            }
//
//        }


    }
}