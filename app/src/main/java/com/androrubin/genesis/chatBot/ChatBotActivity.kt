package com.androrubin.genesis.chatBot

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androrubin.genesis.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatBotActivity : AppCompatActivity() {
    private lateinit var chatsRV: RecyclerView
    private lateinit var userMsgEdt: EditText
    private val BOT_KEY = "bot"
    private lateinit var chatsModalArrayList: ArrayList<ChatsModal>
    private lateinit var chatsRVAdapter: ChatRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)

        // on below line we are initializing all our views.
        chatsRV = findViewById<RecyclerView>(R.id.idRVChats)
        val sendMsgIB = findViewById<FloatingActionButton>(R.id.idFABSend)
        userMsgEdt = findViewById<EditText>(R.id.idEdtMessage)
        chatsModalArrayList = ArrayList<ChatsModal>()
        chatsRVAdapter = ChatRVAdapter(chatsModalArrayList)
        chatsRV.setLayoutManager(LinearLayoutManager(this))
        chatsModalArrayList.add(ChatsModal("Hi! how may I help you? ", BOT_KEY))
        chatsRV.setAdapter(chatsRVAdapter)
        chatsRVAdapter.notifyItemInserted(chatsModalArrayList!!.size - 1)

        // adding on click listener for send message button.
        sendMsgIB.setOnClickListener(View.OnClickListener { // checking if the message entered
            // by user is empty or not.
            if (userMsgEdt.getText().toString().isEmpty()) {
                // if the edit text is empty display a toast message.
                Toast.makeText(
                    this@ChatBotActivity,
                    "Please enter your message...",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            getResponse(userMsgEdt.text.toString())
            // below line we are setting text in our edit text as empty
            userMsgEdt.setText("")
        })
    }

    private fun getResponse(message: String) {
        // below line is to pass message to our
        // array list which is entered by the user.
        val USER_KEY = "user"
        chatsModalArrayList!!.add(ChatsModal(message, USER_KEY))
        chatsRVAdapter.notifyItemInserted(chatsModalArrayList!!.size - 1)

        // url for our brain
        // make sure to add mshape for uid.
        // make sure to add your url.
        val url = """
            
            http://api.brainshop.ai/get?bid=173331&key=pNkDalUOuZmmwzUg&uid=[uid]&msg=$message
            """.trimIndent()
        val BASE_URL = "http://api.brainshop.ai/"
        // creating a variable for our request queue.
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitAPI: RetrofitAPI = retrofit.create(RetrofitAPI::class.java)
        val call: Call<MsgModal?>? = retrofitAPI.getMessage(url)
        if (call != null) {
            call.enqueue(object : Callback<MsgModal?> {
                override fun onResponse(call: Call<MsgModal?>, response: Response<MsgModal?>) {
                    if (response.isSuccessful()) {
                        val modal: MsgModal? = response.body()
                        chatsModalArrayList!!.add(ChatsModal(modal?.cnt.toString(), BOT_KEY))
                        chatsRVAdapter.notifyItemInserted(chatsModalArrayList!!.size - 1)
                    }
                }

                override fun onFailure(call: Call<MsgModal?>, t: Throwable) {
                    chatsModalArrayList!!.add(ChatsModal("Please revert your question", BOT_KEY))
                    chatsRVAdapter.notifyItemInserted(chatsModalArrayList!!.size - 1)
                    Toast.makeText(this@ChatBotActivity, t.message, Toast.LENGTH_LONG).show()
                    Log.d("Main", t.message!!)
                }
            })
        }
    }
}
