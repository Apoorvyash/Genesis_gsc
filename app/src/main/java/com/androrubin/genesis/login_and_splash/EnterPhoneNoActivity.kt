package com.androrubin.genesis.login_and_splash

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.androrubin.genesis.R
import com.androrubin.genesis.databinding.ActivityEnterPhoneNoBinding
import com.androrubin.genesis.ui.home.HomeFragment
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class EnterPhoneNoActivity : AppCompatActivity() {


    private lateinit var binding: ActivityEnterPhoneNoBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var number: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEnterPhoneNoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()

        val mProgressBar=binding.phoneProgressBar
        mProgressBar.visibility = View.INVISIBLE

        binding.sendOTPBtn.setOnClickListener {

            number = binding.phoneEditTextNumber.text.trim().toString()
            if(number.isNotEmpty()){
                if(number.length==10){

                    number = "+91$number"
                    mProgressBar.visibility= View.VISIBLE

                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)

                }else{
                    Toast.makeText(this,"Please Enter correct Number", Toast.LENGTH_SHORT).show()

                }
            }else{

                Toast.makeText(this,"Please Enter Number", Toast.LENGTH_SHORT).show()
            }

        }


    }



    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            // Log.d(TAG, "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            //Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.d(ContentValues.TAG, "onVerificationFailed: ${e.toString()}")
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.d(ContentValues.TAG, "onVerificationFailed: ${e.toString()}")
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            // Log.d(TAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            val intent = Intent(this@EnterPhoneNoActivity, GetOtpActivity::class.java)
            intent.putExtra("OTP",verificationId)
            intent.putExtra("resendToken",token)
            intent.putExtra("phoneNumber",number)
            startActivity(intent)
            binding.phoneProgressBar.visibility = View.INVISIBLE
        }

    }

    override fun onStart(){
        super.onStart()
        if(auth.currentUser !=null){
            startActivity(Intent(this,GetOtpActivity::class.java))
        }

    }

    private fun sendToMain(){
        startActivity(Intent(this,HomeFragment::class.java))
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    Toast.makeText(this,"Authenticate Successfully", Toast.LENGTH_SHORT).show()
                    sendToMain()
                } else {
                    Log.d("TAG", "signInWithAuthCredential:${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }


}