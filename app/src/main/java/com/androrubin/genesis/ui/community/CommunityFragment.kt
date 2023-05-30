package com.androrubin.genesis.ui.community

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.androrubin.genesis.databinding.FragmentCommunityBinding
import com.androrubin.genesis.ui.community.adaptersAndDC.CommunityDC
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityBinding? = null
    private lateinit var db:FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var postList: ArrayList<CommunityDC>
    lateinit var posterName: Array<String>
    lateinit var description: Array<String>
    lateinit var upvotes: Array<String>
    lateinit var downvotes: Array<String>
    lateinit var comments: Array<String>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val dashboardViewModel = ViewModelProvider(this).get(CommunityViewModel::class.java)

        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        val root: View = binding.root

        postList = arrayListOf<CommunityDC>()
        binding.postsRv.layoutManager = LinearLayoutManager(context)
        //getUserData()
        EventChangeListener()
       // binding.postsRv.adapter = CommunityAdapter(postList)
        return root
    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("Community Posts")
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

                            postList.add(dc.document.toObject(CommunityDC::class.java))
                        }
                    }

                    val adapter = CommunityAdapter(postList)
                    binding.postsRv.adapter = adapter
                    adapter.notifyDataSetChanged()
                    binding.createPostBtn.setOnClickListener {
                        startActivity(Intent(context , CreatePostActivity::class.java))
                    }

                    adapter.setOnItemClickListener(object : CommunityAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {

                            // Toast.makeText(context, "You Clicked on item no. $position", Toast.LENGTH_SHORT) .show()

                            val intent = Intent(context,PostViewActivity::class.java)
                            intent.putExtra("posterName",postList[position].posterName)
                            intent.putExtra("postID",postList[position].postID)
                            intent.putExtra("description",postList[position].description)
                            intent.putExtra("date",postList[position].date)
                            intent.putExtra("upvotes",postList[position].upvotes)
                            intent.putExtra("comments",postList[position].comments+" Comments")
                            startActivity(intent)
                        }
                    })
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}