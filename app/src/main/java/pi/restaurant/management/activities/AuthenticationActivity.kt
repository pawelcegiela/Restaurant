package pi.restaurant.management.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.data.User
import pi.restaurant.management.data.UserBasic
import pi.restaurant.management.data.UserDetails
import pi.restaurant.management.databinding.ActivityAuthenticationBinding


class AuthenticationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationBinding
    private lateinit var auth: FirebaseAuth
    private var keepSplashScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition(SplashScreen.KeepOnScreenCondition {
            return@KeepOnScreenCondition keepSplashScreen
        })

        if (intent.getBooleanExtra("logOut", false)) {
            logOut()
        } else {
            authenticate()
        }

        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setButtonLogInListener()
        setResetPasswordListener()
    }

    private fun logOut() {
        Firebase.auth.signOut()
        startActivity(Intent(this, AuthenticationActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        finish()
    }

    private fun authenticate() {
        auth = Firebase.auth
        val user = auth.currentUser
        if (user == null) {
            keepSplashScreen = false
            return
        }

        val databaseRef = Firebase.database.getReference("users").child("basic").child(user.uid)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<UserBasic>()
                if (data == null) {
                    addUserToDatabase()
                }
                if (data == null || !data.disabled) {
                    startMainActivity()
                } else {
                    keepSplashScreen = false
                    Toast.makeText(
                        this@AuthenticationActivity,
                        getString(R.string.account_has_been_disabled),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun addUserToDatabase() {
        val basic =
            UserBasic(Firebase.auth.uid!!, getString(R.string.temp_first_name), getString(R.string.temp_last_name), 3, false)
        val details = UserDetails(Firebase.auth.uid!!, Firebase.auth.currentUser?.email!!)
        Firebase.database.getReference("users").child("basic").child(Firebase.auth.uid!!).setValue(basic)
        Firebase.database.getReference("users").child("details").child(Firebase.auth.uid!!).setValue(details)
        Toast.makeText(
            this@AuthenticationActivity,
            getString(R.string.no_user_data_found),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun setButtonLogInListener() {
        binding.buttonLogIn.setOnClickListener {
            val email = binding.editTextLogInEmail.text.toString()
            val password = binding.editTextLogInPassword.text.toString()

            if (email == "" || password == "") {
                Toast.makeText(this, getString(R.string.enter_email_password), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        authenticate()
                    } else {
                        binding.editTextLogInPassword.setText("")
                        Toast.makeText(
                            baseContext, getString(R.string.authentication_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun setResetPasswordListener() {
        binding.buttonResetPassword.setOnClickListener {
            val email = binding.editTextResetPassEmail.text.toString()
            if (email == "") {
                Toast.makeText(this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        binding.editTextResetPassEmail.setText("")
                        Toast.makeText(
                            this,
                            getString(R.string.password_reset_email_sent),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}