package com.androrubin.genesis.ui.community

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androrubin.genesis.R
import com.androrubin.genesis.ui.community.adaptersAndDC.CommentsDC
import com.androrubin.genesis.ui.community.adaptersAndDC.CommunityDC
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PostViewActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    lateinit var commentorNameList: Array<String>
    lateinit var commentsList: Array<String>
    lateinit var comments: ArrayList<CommentsDC>
    private lateinit var posterName: TextView
    private lateinit var description: TextView
    private lateinit var upvotes: TextView
    private lateinit var downvotes: TextView
    private lateinit var noOfcomments: TextView
    private lateinit var commentsRV: RecyclerView
    private lateinit var commentPostEdt: EditText
    private lateinit var postBtn: ImageView
    private var postID: String?=null
    lateinit var adapter: CommentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_view)

        commentorNameList = arrayOf(
            "Amelia",
            "Ava",
            "Avery",
            "Asher",
            "Aiden",
            "Abigail",
            "Anthony",
            "Aria",
            "Aurora",
            "Angel"
        )

        commentsList = arrayOf(
            "Biden aims to expand vaccines for adults and children",
            "Just got my first shot, helping the world to be a safer place",
            "Local trains to be suspended in Bengal from tomorrow in view of covid-19",
            "MHA asks states,UTs to ensure there are no fires in hospitals",
            "Australian citizen sues PM Morrison over flight ban from india",
            "Former India hockey coach Kaushik hospitalised after testing positive for COVID",
            "Delhi records 20,960 fresh covid-19 infections, positivity rate at 26.37%",
            "Barcelona church offers open-air space for Ramadan",
            "Trillions of cicadas set to emerge in the US, here's why",
            "Homemaker, economist: Candidates from all walks of life in Bengal assembly"
        )

        posterName = findViewById(R.id.posterNameEdt)
        description = findViewById(R.id.descriptionEdt)
        upvotes = findViewById(R.id.upvotesEdt)
        downvotes = findViewById(R.id.downvotesEdt)
        noOfcomments = findViewById(R.id.commentsEdt)
        commentsRV = findViewById(R.id.commentsRv)
        commentPostEdt = findViewById(R.id.enterCommentEdt)
        postBtn = findViewById(R.id.commentSendBtn)

        commentsRV.layoutManager = LinearLayoutManager(this)
        posterName.text = intent.getStringExtra("posterName")
        description.text = intent.getStringExtra("description")
        upvotes.text = intent.getStringExtra("upvotes")
        downvotes.text = intent.getStringExtra("downvotes")
        noOfcomments.text = intent.getStringExtra("comments")
        postID = intent.getStringExtra("postID")

        comments = arrayListOf<CommentsDC>()

        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:SS")
        val current = formatter.format(time)
        getComments()
        adapter = CommentsAdapter(comments)
        commentsRV.adapter = adapter

        if (TextUtils.isEmpty(commentPostEdt.getText()?.trim().toString())) {
            postBtn.isClickable = false
            postBtn.setImageResource(R.drawable.baseline_send_24)
        } else {
            postBtn.isClickable = true
            postBtn.setImageResource(R.drawable.baseline_send_active_24)
        }
        postBtn.setOnClickListener {

            val data = hashMapOf(
                "commentorName" to posterName.text.trim().toString(),
                "comment" to commentPostEdt.text.trim().toString(),
                "date" to current.toString(),
            )
            db = FirebaseFirestore.getInstance()
            db.collection("Community Posts").document("$postID").collection("Comments")
                .add(data)
                .addOnSuccessListener { docRef ->
                    Log.d("Data Addition", "DocumentSnapshot written with ID: ${docRef}.id")
                    //Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()


                }
                .addOnFailureListener { e ->
                    Log.w("Data Addition", "Error adding document", e)
                }
            adapter.notifyDataSetChanged()
            commentPostEdt.setText("")
            //hideKeyboard()
        }
    }
//    fun Activity.hideKeyboard() {
//
//        hideKeyboard(currentFocus ?: View(this))
//    }

    private fun getComments() {
        db = FirebaseFirestore.getInstance()
        db.collection("Community Posts").document("$postID").collection("Comments")
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {

                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {

                            comments.add(dc.document.toObject(CommentsDC::class.java))
                        }
                    }
                   adapter.notifyDataSetChanged()
                }
            })
    }
}

