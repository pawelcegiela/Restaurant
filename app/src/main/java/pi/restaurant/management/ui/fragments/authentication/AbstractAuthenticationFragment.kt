package pi.restaurant.management.ui.fragments.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.databinding.FragmentAuthenticationBinding
import pi.restaurant.management.ui.activities.AuthenticationActivity


abstract class AbstractAuthenticationFragment : Fragment() {
    private var _binding: FragmentAuthenticationBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonLogInListener()
        setResetPasswordListener()
    }

    private fun setButtonLogInListener() {
        binding.buttonLogIn.setOnClickListener {
            val email = binding.editTextLogInEmail.text.toString()
            val password = binding.editTextLogInPassword.text.toString()

            if (email == "" || password == "") {
                Toast.makeText(requireContext(), getString(R.string.enter_email_password), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        (activity as AuthenticationActivity).authenticate {
                            if (this is WorkerAuthenticationFragment) {
                                (activity as AuthenticationActivity).loginAsWorker(it)
                            } else if (this is CustomerAuthenticationFragment) {
                                (activity as AuthenticationActivity).loginAsCustomer(it)
                            }
                        }
                    } else {
                        binding.editTextLogInPassword.setText("")
                        Toast.makeText(
                            requireContext(), getString(R.string.authentication_failed),
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
                Toast.makeText(requireContext(), getString(R.string.enter_email), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        binding.editTextResetPassEmail.setText("")
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.password_reset_email_sent),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}