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
import pi.restaurantapp.databinding.FragmentClientOrderSummaryBinding
import pi.restaurantapp.databinding.ToolbarNavigationPreviewBinding
import pi.restaurantapp.viewmodels.activities.client.ClientNewOrderViewModel
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.viewmodels.fragments.client.neworder.ClientOrderSummaryViewModel
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.ui.dialogs.RedeemDiscountDialog
import pi.restaurantapp.ui.fragments.AbstractPreviewItemFragment
import pi.restaurantapp.logic.utils.ComputingUtils
import pi.restaurantapp.logic.utils.StringFormatUtils

class ClientOrderSummaryFragment : AbstractPreviewItemFragment() {
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation: ToolbarNavigationPreviewBinding get() = binding.toolbarNavigation
    override val editActionId = 0 // Warning: unused
    override val backActionId = 0 // Warning: unused
    override val viewModel: AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel: ClientOrderSummaryViewModel by viewModels()
    val activityViewModel: ClientNewOrderViewModel by activityViewModels()

    private var _binding: FragmentClientOrderSummaryBinding? = null
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
        _binding = FragmentClientOrderSummaryBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.fragment = this
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun fillInData() {
        _viewModel.initializeData()
    }

    override fun addLiveDataObservers() {
        super.addLiveDataObservers()

        _viewModel.possibleDiscounts.observe(viewLifecycleOwner) { list ->
            binding.buttonRedeemDiscount.setOnClickListener {
                RedeemDiscountDialog(requireContext(), list, getString(R.string.select_discount)) { discount ->
                    redeemDiscount(discount)
                }
            }
        }
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