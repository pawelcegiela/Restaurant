package pi.restaurant.management.fragments.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.activities.MainActivity
import pi.restaurant.management.databinding.FragmentMyDataBinding

class MyDataFragment : Fragment() {
    private var _binding: FragmentMyDataBinding? = null
    private val binding get() = _binding!!
    private var firstTime: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firstTime = activity!!.intent.getBooleanExtra("firstTime", false)

        loadData()
        setButtonListener()
    }

    private fun loadData() {
        val user = Firebase.auth.currentUser ?: return
        val databaseRef = Firebase.database.getReference("users").child(user.uid)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue<HashMap<String, String>>()
                if (value != null) {
                    binding.editTextFirstName.setText(value["firstName"])
                    binding.editTextLastName.setText(value["lastName"])
                }

                if (user.email != null) {
                    binding.editTextEmail.setText(user.email)
                    binding.editTextEmail.isEnabled = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("MyData", "Failed to read value.", error.toException())
            }
        })
    }

    private fun setButtonListener() {
        val user = Firebase.auth.currentUser ?: return
        val databaseRef = Firebase.database.getReference("users").child(user.uid)

        binding.buttonSaveData.setOnClickListener {
            val firstName = binding.editTextFirstName.text.toString()
            val lastName = binding.editTextLastName.text.toString()

            if (firstName.trim().isEmpty()) {
                binding.editTextFirstName.error =
                    getString(R.string.is_required, getString(R.string.first_name))
            }
            if (lastName.trim().isEmpty()) {
                binding.editTextLastName.error =
                    getString(R.string.is_required, getString(R.string.last_name))
            }
            if (firstName.trim().isEmpty() || lastName.trim().isEmpty()) {
                return@setOnClickListener
            }

            val map = HashMap<String, String>()
            map["firstName"] = firstName
            map["lastName"] = lastName

            databaseRef.setValue(map)

            if (firstTime) {
                startActivity(Intent(activity, MainActivity::class.java))
            } else {
                Toast.makeText(
                    activity,
                    getString(R.string.your_data_has_been_changed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}