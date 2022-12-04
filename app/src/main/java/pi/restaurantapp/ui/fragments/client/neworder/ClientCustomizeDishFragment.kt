package pi.restaurantapp.ui.fragments.client.neworder

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import pi.restaurantapp.R
import pi.restaurantapp.objects.data.address.AddressBasic
import pi.restaurantapp.objects.enums.CollectionType
import pi.restaurantapp.objects.enums.OrderPlace
import pi.restaurantapp.ui.fragments.management.orders.CustomizeDishFragment
import pi.restaurantapp.viewmodels.activities.client.ClientNewOrderViewModel


class ClientCustomizeDishFragment : CustomizeDishFragment() {
    override val activityViewModel: ClientNewOrderViewModel by activityViewModels()

    override fun initializeNavigationToolbar() {
        toolbarNavigation.cardAdd.root.setOnClickListener {
            val dataObject = getDataObject()
            ensureSavedOrderExists()
            activityViewModel.addDishItem(dataObject)
            Toast.makeText(
                requireContext(),
                getString(R.string.current_number_of_positions, activityViewModel.savedOrder.value!!.details.dishes.size),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().popBackStack()
        }
    }

    private fun ensureSavedOrderExists() {
        if (activityViewModel.savedOrder.value == null) {
            val preferences = activity?.getSharedPreferences("prefs", Context.MODE_PRIVATE) ?: return
            val address = AddressBasic(
                city = preferences.getString("city", "") ?: "",
                postalCode = preferences.getString("postalCode", "") ?: "",
                street = preferences.getString("street", "") ?: "",
                houseNumber = preferences.getString("houseNumber", "") ?: "",
                flatNumber = preferences.getString("flatNumber", "") ?: ""
            )
            val contactPhone = preferences.getString("contactPhone", "") ?: ""
            val collectionType = preferences.getInt("collectionType", CollectionType.DELIVERY.ordinal)
            val orderPlace = preferences.getInt("orderPlace", OrderPlace.TO_GO.ordinal)
            activityViewModel.createSavedOrder(address, contactPhone, collectionType, orderPlace)
        }
    }
}