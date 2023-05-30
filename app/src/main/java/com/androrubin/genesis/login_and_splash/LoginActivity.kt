package com.androrubin.genesis.login_and_splash

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.androrubin.genesis.MainActivity
import com.androrubin.genesis.R
import com.androrubin.genesis.databinding.ActivityLoginBinding
import com.androrubin.genesis.ui.home.HomeFragment
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    companion object {
        private const val RC_SIGN_IN = 100

    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var mCallbackManager: CallbackManager
    private lateinit var fbButton: LoginButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        FacebookSdk.sdkInitialize(this)

        db = FirebaseFirestore.getInstance()
        fbButton = findViewById(R.id.face_book)
//        fbButton.setBackgroundResource(R.drawable.face2)
//        fbButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)


        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create()

        fbButton.setPermissions("email", "public_profile")
        fbButton.registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(ContentValues.TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(ContentValues.TAG, "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d(ContentValues.TAG, "facebook:onError", error)
            }
        })













        firebaseAuth = FirebaseAuth.getInstance()


        binding.phoneAuth.setOnClickListener {
            val intent = Intent(this, EnterPhoneNoActivity::class.java)
            startActivity(intent)
        }

        binding.signInNext.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.continueBtn.setOnClickListener {
            val email = binding.emailText.text.toString()
            val pass = binding.passTxt.text.toString()


            if (email.isNotEmpty() && pass.isNotEmpty()) {


                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {

                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }

                }
            } else {
                Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            }
        }


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()


        binding.googleSignin.setOnClickListener {
            signIn()
        }

    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInClient.signOut()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {

                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("LoginActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("LoginActivity", "Google sign in failed", e)
                }
            } else {
                Log.w("LoginActivity", exception.toString())
            }

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    updateUI()
                        }
                 else {
                    // If sign in fails, display a message to the user
                    Log.w("LoginActivity", "signInWithCredential:failure", task.exception)
                }
            }
    }

    fun onActivityResult2(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
    }


    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(ContentValues.TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")
                    updateUI()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        this, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI()
                }
            }
    }

    private fun updateUI() {

        val currentUser = firebaseAuth.currentUser
        val uid = currentUser?.uid

        db = FirebaseFirestore.getInstance()
        db.collection("Users").document("$uid")
            .get()
            .addOnSuccessListener {

                //Returns value of corresponding field
                val user = it["ProfileCreated"].toString()

                if (user=="1") {

                    val dashboardIntent = Intent(this, MainActivity::class.java)
                    startActivity(dashboardIntent)
                    finish()

                }
                else {

                    val dashboardIntent = Intent(this,CreateProfile::class.java)
                    startActivity(dashboardIntent)
                    finish()
                }
            }

    }

}
