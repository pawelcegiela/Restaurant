package pi.restaurantapp.ui.fragments.authentication

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.R
import pi.restaurantapp.objects.data.address.AddressBasic
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.data.user.UserDetails
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.ui.activities.AuthenticationActivity
import java.util.*


class CustomerAuthenticationFragment : AbstractAuthenticationFragment() {
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
                Firebase.auth.createUserWithEmailAndPassword(email.toString(), password.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        saveNewCustomer()
                        (activity as AuthenticationActivity).startMainActivity(Role.CUSTOMER.ordinal)
                    } else {
                        Log.e("Error", "Didn't create account")
                    }
                }
            }
        }
    }

    private fun saveNewCustomer() {
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
            creationDate = Date(),
            ordersToDeliver = HashMap(),
            defaultDeliveryAddress = AddressBasic(),
            contactPhone = ""
        )

        Firebase.database.getReference("users").child("basic").child(userId).setValue(basic)
        Firebase.database.getReference("users").child("details").child(userId).setValue(details)
    }
}