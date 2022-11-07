package pi.restaurantapp.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.R
import pi.restaurantapp.databinding.ActivityAuthenticationBinding
import pi.restaurantapp.model.activities.AuthenticationViewModel
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.data.user.UserDetails
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.ui.activities.client.ClientMainActivity
import pi.restaurantapp.ui.activities.management.MainActivity


class AuthenticationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationBinding
    private var keepSplashScreen = true
    val viewModel: AuthenticationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition(SplashScreen.KeepOnScreenCondition {
            return@KeepOnScreenCondition keepSplashScreen
        })

        if (intent.getBooleanExtra("logOut", false)) {
            logOut()
        } else {
            authenticate { data ->
                if (data == null || data.disabled) {
                    keepSplashScreen = false
                    Toast.makeText(
                        this@AuthenticationActivity,
                        getString(R.string.authentication_failed),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    startMainActivity(data.role)
                }
            }
        }

        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar.toolbar)
    }

    private fun logOut() {
        Firebase.auth.signOut()
        startActivity(Intent(this, AuthenticationActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        finish()
    }

    fun authenticate(callback: (UserBasic?) -> (Unit)) {
        val user = Firebase.auth.currentUser
        if (user == null) {
            keepSplashScreen = false
            return
        }

        val databaseRef = Firebase.database.getReference("users").child("basic").child(user.uid)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<UserBasic>()
                callback(data)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun loginAsWorker(data: UserBasic?) {
        if (data == null || !Role.isWorkerRole(data.role)) {
            keepSplashScreen = false
            Toast.makeText(
                this@AuthenticationActivity,
                getString(R.string.worker_account_no_data),
                Toast.LENGTH_LONG
            ).show()
            Firebase.auth.signOut()
        } else if (data.disabled) {
            keepSplashScreen = false
            Toast.makeText(
                this@AuthenticationActivity,
                getString(R.string.account_has_been_disabled),
                Toast.LENGTH_LONG
            ).show()
        } else {
            startMainActivity(data.role)
        }
    }

    fun loginAsCustomer(data: UserBasic?) {
        if (data == null || Role.isWorkerRole(data.role)) {
            keepSplashScreen = false
            Toast.makeText(
                this@AuthenticationActivity,
                getString(R.string.customer_account_no_data),
                Toast.LENGTH_LONG
            ).show()
            Firebase.auth.signOut()
        } else if (data.disabled) {
            keepSplashScreen = false
            Toast.makeText(
                this@AuthenticationActivity,
                getString(R.string.account_has_been_disabled),
                Toast.LENGTH_LONG
            ).show()
        } else {
            startMainActivity(data.role)
        }
    }

    fun startMainActivity(role: Int) {
        if (Role.isWorkerRole(role)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            val databaseRef = Firebase.database.getReference("users").child("details").child(Firebase.auth.uid!!)
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val data = dataSnapshot.getValue<UserDetails>() ?: return
                    changeSharedPreferences(data)
                    startActivity(Intent(this@AuthenticationActivity, ClientMainActivity::class.java))
                    finish()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    private fun changeSharedPreferences(data: UserDetails) {
        val sharedPref = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("city", data.defaultDeliveryAddress.city)
            putString("postalCode", data.defaultDeliveryAddress.postalCode)
            putString("street", data.defaultDeliveryAddress.street)
            putString("houseNumber", data.defaultDeliveryAddress.houseNumber)
            putString("flatNumber", data.defaultDeliveryAddress.flatNumber)
            putString("contactPhone", data.contactPhone)
            putInt("collectionType", data.preferredCollectionType)
            putInt("orderPlace", data.preferredOrderPlace)
            apply()
        }
    }
}