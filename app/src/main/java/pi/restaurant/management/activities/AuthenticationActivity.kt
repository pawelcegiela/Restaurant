package pi.restaurant.management.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.databinding.ActivityAuthenticationBinding


class AuthenticationActivity : BaseActivity() {
    private lateinit var binding: ActivityAuthenticationBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        auth = Firebase.auth

        setButtonLogInListener()
        setResetPasswordListener()
    }

    public override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            val databaseRef = Firebase.database.getReference("users")
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.child(auth.currentUser!!.uid).exists()) {
                        startMainActivity()
                    } else {
                        startMyDataActivity()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu) = true

    private fun setButtonLogInListener() {
        binding.buttonLogIn.setOnClickListener {
            val email = binding.editTextLogInEmail.text.toString()
            val password = binding.editTextLogInPassword.text.toString()

            if (email == "" || password == "") {
                Toast.makeText(this, getString(R.string.enter_email_password), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        startMainActivity()
                    } else {
                        binding.editTextLogInPassword.setText("")
                        Toast.makeText(baseContext, getString(R.string.authentication_failed),
                            Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(this, getString(R.string.password_reset_email_sent), Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun startMyDataActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        intent.putExtra("firstTime", true)
        startActivity(intent)
        finish()
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}