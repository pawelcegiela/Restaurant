package pi.restaurantapp.ui.fragments.authentication

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import pi.restaurantapp.R

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for WorkerAuthenticationFragment.
 */
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