package pi.restaurant.management.activities

import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.databinding.ActivityMyDataBinding

class MyDataActivity : BaseActivity() {
    private lateinit var binding: ActivityMyDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.my_data)

        setAllData()
    }

    private fun setAllData() {
        val user = Firebase.auth.currentUser
        if (user != null && user.email != null) {
            binding.editTextFirstName
            binding.editTextLastName
            binding.editTextEmail.setText(user.email)
        }
    }
}