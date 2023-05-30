package com.androrubin.genesis.login_and_splash

import android.content.ContentValues
import android.content.Intent
import com.androrubin.genesis.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.androrubin.genesis.MainActivity
import com.androrubin.genesis.databinding.ActivityGetOtpBinding
import com.androrubin.genesis.ui.home.HomeFragment
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class GetOtpActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGetOtpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar


    private lateinit var OTP:String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneNumber:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()

        OTP= intent.getStringExtra("OTP").toString()
        resendToken= intent.getParcelableExtra("resendToken")!!
        phoneNumber = intent.getStringExtra("phoneNumber")!!


        progressBar=findViewById(R.id.otpProgressBar)
        progressBar.visibility = View.INVISIBLE
        addTextChangeListener()
        resendOTPVisibility()

        binding.resendTextView.setOnClickListener {
            resendVerificationCode()
            resendOTPVisibility()
        }

        binding.verifyOTPBtn.setOnClickListener {
            //collect otp from the edit text

            val typedOtp = (binding.otpEditText1.text.toString() +
                    binding.otpEditText2.text.toString() +
                    binding.otpEditText3.text.toString() +
                    binding.otpEditText4.text.toString() +
                    binding.otpEditText5.text.toString() +
                    binding.otpEditText6.text.toString() )

            if(typedOtp.isNotEmpty()){
                if(typedOtp.length==6){

                    val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        OTP ,typedOtp
                    )
                    progressBar.visibility = View.VISIBLE
                    signInWithPhoneAuthCredential(credential)
                }else{
                    Toast.makeText(this,"Please Enter Correct OTP", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Please Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private  fun resendOTPVisibility(){

        binding.otpEditText1.setText("")
        binding.otpEditText2.setText("")
        binding.otpEditText3.setText("")
        binding.otpEditText4.setText("")
        binding.otpEditText5.setText("")
        binding.otpEditText6.setText("")
        binding.resendTextView.visibility = View.INVISIBLE
        binding.resendTextView.isEnabled = false

        Handler(Looper.myLooper()!!).postDelayed(Runnable{

            binding.resendTextView.visibility = View.VISIBLE
            binding.resendTextView.isEnabled = true
        },6000)
    }

    private fun sendToMain(){
        startActivity(Intent(this,MainActivity::class.java))
    }

    private fun resendVerificationCode(){

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)
            .setForceResendingToken(resendToken)// OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
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
            progressBar.visibility = View.VISIBLE
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
            OTP = verificationId
            resendToken = token
        }

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
                progressBar.visibility = View.VISIBLE
            }
    }

    private fun addTextChangeListener(){
        binding.otpEditText1.addTextChangedListener(EditTextWatcher(binding.otpEditText1))
        binding.otpEditText2.addTextChangedListener(EditTextWatcher(binding.otpEditText2))
        binding.otpEditText3.addTextChangedListener(EditTextWatcher(binding.otpEditText3))
        binding.otpEditText4.addTextChangedListener(EditTextWatcher(binding.otpEditText4))
        binding.otpEditText5.addTextChangedListener(EditTextWatcher(binding.otpEditText5))
        binding.otpEditText6.addTextChangedListener(EditTextWatcher(binding.otpEditText6))
    }

    inner class EditTextWatcher(private val view: View): TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {


            val text = p0.toString()
            when(view.id){

                R.id.otpEditText1 -> if(text.length==1) {
                    binding.otpEditText2.requestFocus()
                }

               R.id.otpEditText2 -> if(text.length==1){
                    binding.otpEditText3.requestFocus()
                }
                else if(text.isEmpty()){
                    binding.otpEditText1.requestFocus()
                }



                R.id.otpEditText3 -> if(text.length==1){
                    binding.otpEditText4.requestFocus()
                }
                else if(text.isEmpty()){
                    binding.otpEditText2.requestFocus()
                }



                R.id.otpEditText4 -> if(text.length==1){
                    binding.otpEditText5.requestFocus()
                }
                else if(text.isEmpty()){
                    binding.otpEditText3.requestFocus()
                }



                R.id.otpEditText5 -> if(text.length==1){
                    binding.otpEditText6.requestFocus()
                }
                else if(text.isEmpty()){
                    binding.otpEditText4.requestFocus()
                }

                R.id.otpEditText6 ->  if(text.isEmpty()){
                    binding.otpEditText5.requestFocus()
                }


            }
        }

    }
}