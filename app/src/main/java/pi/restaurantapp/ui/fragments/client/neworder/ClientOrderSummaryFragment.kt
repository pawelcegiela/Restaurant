package pi.restaurantapp.ui.fragments.client.neworder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentClientPreviewOrderBinding
import pi.restaurantapp.databinding.ToolbarNavigationPreviewBinding
import pi.restaurantapp.model.activities.client.ClientNewOrderViewModel
import pi.restaurantapp.model.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.model.fragments.client.neworder.ClientOrderSummaryViewModel
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.enums.CollectionType
import pi.restaurantapp.objects.enums.OrderPlace
import pi.restaurantapp.ui.RecyclerManager
import pi.restaurantapp.ui.adapters.OrderDishesRecyclerAdapter
import pi.restaurantapp.ui.fragments.AbstractPreviewItemFragment
import pi.restaurantapp.ui.listeners.RedeemDiscountButtonListener
import pi.restaurantapp.utils.ComputingUtils
import pi.restaurantapp.utils.StringFormatUtils

class ClientOrderSummaryFragment : AbstractPreviewItemFragment() {
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation: ToolbarNavigationPreviewBinding get() = binding.toolbarNavigation
    override val editActionId = 0 // Warning: unused
    override val backActionId = 0 // Warning: unused
    override val viewModel: AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel: ClientOrderSummaryViewModel by viewModels()
    val activityViewModel: ClientNewOrderViewModel by activityViewModels()

    private var _binding: FragmentClientPreviewOrderBinding? = null
    val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (activityViewModel.savedOrder.value == null) {
            super.onViewCreated(view, savedInstanceState)
        } else {
            viewModel.itemId = activityViewModel.savedOrder.value?.id ?: ""
            _viewModel.setItem(activityViewModel.savedOrder.value ?: return)
            activityViewModel.reset()
            addLiveDataObservers()
            viewModel.getUserRole()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientPreviewOrderBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun fillInData() {
        val item = _viewModel.item.value ?: Order()

        binding.textViewStatusTitle.visibility = View.GONE
        binding.textViewStatus.visibility = View.GONE
        binding.textViewModificationDateTitle.visibility = View.GONE
        binding.textViewModificationDate.visibility = View.GONE
        binding.textViewDeliveryPersonTitle.visibility = View.GONE
        binding.textViewDeliveryPerson.visibility = View.GONE
        binding.buttonCancelOrder.visibility = View.GONE
        binding.cardStatusHistory.visibility = View.GONE

        binding.textViewName.text = item.basic.name
        binding.textViewOrderDate.text = StringFormatUtils.formatDateTime(item.details.orderDate)
        binding.textViewCollectionDate.text = StringFormatUtils.formatDateTime(item.basic.collectionDate)

        val dishesList = item.details.dishes.toList().map { it.second }.toMutableList()
        binding.recyclerViewDishes.adapter = OrderDishesRecyclerAdapter(dishesList, this)
        binding.recyclerViewDishes.layoutManager = RecyclerManager(context)

        binding.textViewDelivery.text = CollectionType.getString(item.basic.collectionType, requireContext())
        binding.textViewPlace.text = OrderPlace.getString(item.details.orderPlace, requireContext())

        if (item.details.comments.isNotEmpty()) {
            binding.textViewComments.text = item.details.comments
        }

        if (item.basic.collectionType == CollectionType.DELIVERY.ordinal && item.details.address != null) {
            binding.textViewDeliveryAddress.text = StringFormatUtils.formatAddress(item.details.address!!)
        } else {
            binding.textViewDeliveryAddressTitle.visibility = View.GONE
            binding.textViewDeliveryAddress.visibility = View.GONE
        }

        binding.textViewFullPrice.text = StringFormatUtils.formatPrice(item.basic.value)
        if (item.details.contactPhone.isNotEmpty()) {
            binding.textViewContactPhone.text = item.details.contactPhone
        }

        binding.buttonRedeemDiscount.visibility = View.VISIBLE

        _viewModel.getPossibleDiscounts()
        _viewModel.possibleDiscounts.observe(viewLifecycleOwner) { list ->
            binding.buttonRedeemDiscount.setOnClickListener(
                RedeemDiscountButtonListener(list,
                    this)
            )
        }

        viewModel.setReadyToUnlock()
    }

    override fun initializeWorkerUI() {
        toolbarNavigation.root.visibility = View.VISIBLE

        toolbarNavigation.cardBack.root.setOnClickListener {
            findNavController().navigate(R.id.actionClientOrderSummaryToMainActivity)
        }
    }

    fun redeemDiscount(discount: DiscountBasic) {
        _viewModel.redeemDiscount(_viewModel.item.value!!.basic.value, discount) { success ->
            if (success) {
                binding.buttonRedeemDiscount.visibility = View.GONE
                binding.textViewDiscount.text = discount.id

                val newPrice = ComputingUtils.countPriceAfterDiscount(_viewModel.item.value!!.basic.value, discount)
                binding.textViewFullPrice.text = StringFormatUtils.formatPrice(newPrice)
            } else {
                Toast.makeText(requireContext(), getString(R.string.couldnt_redeem_discount), Toast.LENGTH_SHORT).show()
            }
        }
    }
}