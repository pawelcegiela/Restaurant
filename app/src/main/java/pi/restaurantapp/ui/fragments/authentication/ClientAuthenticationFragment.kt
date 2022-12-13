package pi.restaurantapp.ui.fragments.authentication

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.R
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.data.user.UserDetails
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.ui.activities.AuthenticationActivity
import java.util.*

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for ClientAuthenticationFragment.
 */
class ClientAuthenticationFragment : AbstractAuthenticationFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLogInAsWorker.setOnClickListener {
            findNavController().navigate(R.id.actionCustomerAuthenticationToWorker)
        }
        setSignInButtonListener()
    }

    private fun setSignInButtonListener() {
        binding.buttonSignIn.setOnClickListener {
            val email = binding.editTextSignInEmail.text
            val password = binding.editTextSignInPassword.text
            val repeatPassword = binding.editTextSignInPassword.text
            // TODO Checking everything - common logic with adding new worker
            if (email.isNotEmpty() && password.isNotEmpty() && password == repeatPassword) {
                activityViewModel.createCustomer(email.toString(), password.toString(), onSuccess = {
                    activity?.getSharedPreferences("prefs", Context.MODE_PRIVATE)?.edit()?.clear()?.apply()
                    val name = saveNewCustomer()
                    (activity as AuthenticationActivity).startMainActivity(Role.CUSTOMER.ordinal, name)
                }, onFailure = {
                    Toast.makeText(requireContext(), getString(R.string.could_not_create_account), Toast.LENGTH_SHORT).show()
                })
            }
        }
    }

    private fun saveNewCustomer(): String {
        val userId = Firebase.auth.uid!!
        val email = Firebase.auth.currentUser?.email!!
        val temporaryName = email.substringBefore('@')

        val basic = UserBasic(
            id = userId,
            firstName = getString(R.string.customer),
            lastName = temporaryName,
            role = Role.CUSTOMER.ordinal,
            disabled = false,
            delivery = false
        )
        val details = UserDetails(
            id = userId,
            email = email,
            creationDate = Date()
        )

        activityViewModel.addUserDataToDatabase(userId, basic, details)
        activity?.getSharedPreferences("prefs", Context.MODE_PRIVATE)?.edit()?.putString("name", basic.getFullName())?.apply()

        return basic.getFullName()
    }
}