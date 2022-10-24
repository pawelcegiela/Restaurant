package pi.restaurant.management.ui.fragments.authentication

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.ui.activities.AuthenticationActivity


class WorkerAuthenticationFragment : AbstractAuthenticationFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardSignIn.visibility = View.GONE
        binding.buttonLogInAsWorker.text = getText(R.string.log_in_as_a_customer)
        binding.textViewLogIn.text = getText(R.string.log_in_as_a_worker)
        binding.buttonLogInAsWorker.setOnClickListener {
            findNavController().navigate(R.id.actionWorkerAuthenticationToCustomer)
        }
    }
}