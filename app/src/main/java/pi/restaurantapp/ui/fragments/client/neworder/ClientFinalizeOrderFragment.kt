package pi.restaurantapp.ui.fragments.client.neworder

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import pi.restaurantapp.R
import pi.restaurantapp.model.activities.client.ClientNewOrderViewModel
import pi.restaurantapp.model.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.model.fragments.client.neworder.ClientFinalizeOrderViewModel
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails
import pi.restaurantapp.objects.enums.OrderType
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.ui.activities.client.ClientNewOrderActivity
import pi.restaurantapp.ui.fragments.management.orders.AbstractModifyOrderFragment

//TODO Ustawienia z cache'a
class ClientFinalizeOrderFragment : AbstractModifyOrderFragment() {

    override val nextActionId = R.id.actionClientFinalizeOrderToOrderSummary
    override val saveMessageId = R.string.order_added
    override val removeMessageId = 0 // Warning: unused
    override val addDishAction = 0 // Warning: unused
    override val editDishActionId = R.id.actionClientFinalizeOrderToCustomizeDish
    override val viewModel: AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel: ClientFinalizeOrderViewModel by viewModels()

    override val activityViewModel: ClientNewOrderViewModel by activityViewModels()
    override val lowestRole = Role.CUSTOMER.ordinal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val sdo = getDataObject()
                activityViewModel.setSavedOrder(Order(sdo.id, sdo.basic as OrderBasic, sdo.details as OrderDetails))
                findNavController().navigate(R.id.actionClientFinalizeOrderToNewOrder)
            }
        })
    }

    override fun initializeUI() {
        super.initializeUI()
        initializeRecycler()
        setNavigationCardsSave()
        hideElements()
        binding.spinnerType.setSelection(OrderType.CLIENT_APP.ordinal)
        finishLoading()
    }

    private fun hideElements() {
        binding.buttonAddDish.visibility = View.GONE
        binding.textViewTypeTitle.visibility = View.GONE
        binding.spinnerType.visibility = View.GONE
        binding.textViewOrderDate.visibility = View.GONE
        binding.textViewOrderDateTitle.visibility = View.GONE
    }

    override fun removeDish(dishItem: DishItem) {
        super.removeDish(dishItem)
        (activity as ClientNewOrderActivity).binding.toolbar.textViewAdditionalInfo.text = dishesList.size.toString()
    }

    override fun afterSave() {
        val sdo = getDataObject()
        activityViewModel.setSavedOrder(Order(sdo.id, sdo.basic as OrderBasic, sdo.details as OrderDetails))
        super.afterSave()
    }
}