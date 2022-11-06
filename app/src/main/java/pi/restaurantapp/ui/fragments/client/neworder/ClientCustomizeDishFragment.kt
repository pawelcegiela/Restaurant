package pi.restaurantapp.ui.fragments.client.neworder

import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import pi.restaurantapp.R
import pi.restaurantapp.model.activities.client.ClientNewOrderViewModel
import pi.restaurantapp.ui.fragments.management.orders.CustomizeDishFragment


class ClientCustomizeDishFragment : CustomizeDishFragment() {
    override val activityViewModel: ClientNewOrderViewModel by activityViewModels()

    override fun initializeWorkerUI() {
        toolbarNavigation.root.visibility = View.VISIBLE
        toolbarNavigation.cardBack.root.visibility = View.GONE
        toolbarNavigation.cardAdd.root.visibility = View.VISIBLE

        toolbarNavigation.cardAdd.root.setOnClickListener {
            val dataObject = getDataObject()
            activityViewModel.addDishItem(dataObject)
            Toast.makeText(
                requireContext(),
                getString(R.string.current_number_of_positions, activityViewModel.savedOrder.value!!.details.dishes.size),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().popBackStack()
        }
    }

    override fun fillInData() {
        binding.textViewRecipeTitle.visibility = View.GONE
        binding.textViewRecipe.visibility = View.GONE

        super.fillInData()
    }
}