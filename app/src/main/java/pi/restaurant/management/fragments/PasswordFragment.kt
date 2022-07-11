package pi.restaurant.management.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.databinding.FragmentPasswordBinding

class PasswordFragment : Fragment() {
    private var _binding: FragmentPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButtonListener()
    }

    private fun setButtonListener() {
        val user = Firebase.auth.currentUser ?: return

        binding.buttonSavePassword.setOnClickListener {
            val oldPassword = binding.editTextCurrentPassword.text.toString()
            val newPassword = binding.editTextNewPassword.text.toString()
            val newPasswordRepeated = binding.editTextRepeatNewPassword.text.toString()

            binding.editTextCurrentPassword.setText("")

            if (newPassword != newPasswordRepeated) {
                Toast.makeText(activity, getString(R.string.passwords_differ), Toast.LENGTH_SHORT).show()
            } else if (user.email != null) {
                val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)
                binding.editTextNewPassword.setText("")
                binding.editTextRepeatNewPassword.setText("")

                user.reauthenticate(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            user.updatePassword(newPassword).addOnCompleteListener {
                                Toast.makeText(
                                    activity,
                                    getString(R.string.password_changed),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                activity,
                                getString(R.string.auth_failed_try_again),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}