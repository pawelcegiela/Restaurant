package pi.restaurant.management.ui.fragments.management.restaurantdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.databinding.FragmentModifyDeliveryBinding
import pi.restaurant.management.model.fragments.management.AbstractModifyItemViewModel
import pi.restaurant.management.model.fragments.management.restaurantdata.EditDeliveryViewModel
import pi.restaurant.management.objects.data.SplitDataObject
import pi.restaurant.management.objects.data.delivery.Delivery
import pi.restaurant.management.objects.data.delivery.DeliveryBasic
import pi.restaurant.management.objects.data.delivery.DeliveryDetails
import pi.restaurant.management.ui.fragments.management.AbstractModifyItemFragment
import pi.restaurant.management.utils.StringFormatUtils

class EditDeliveryFragment : AbstractModifyItemFragment() {
    private var _binding: FragmentModifyDeliveryBinding? = null
    private val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation get() = binding.toolbarNavigation
    override var itemId = "delivery"
    override val nextActionId = R.id.actionDeliveryToRD
    override val saveMessageId = R.string.delivery_settings_changed
    override val removeMessageId = 0 // Unused

    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : EditDeliveryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyDeliveryBinding.inflate(inflater, container, false)
        binding.linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun initializeUI() {
        initializeCheckBoxListeners()
    }

    override fun fillInData() {
        val data = _viewModel.item.value ?: Delivery()
        binding.checkBoxAvailable.isChecked = data.basic.available
        if (data.basic.available) {
            binding.editTextMinimumPrice.setText(data.basic.minimumPrice)
            binding.editTextExtraFee.setText(data.basic.extraDeliveryFee)
            binding.checkBoxFreeThreshold.isChecked = data.basic.freeDeliveryAvailable
            if (data.basic.freeDeliveryAvailable) {
                binding.editTextFreeThreshold.setText(data.basic.minimumPriceFreeDelivery)
            } else {
                binding.editTextFreeThreshold.isEnabled = false
            }
        } else {
            binding.linearLayoutAvailableDelivery.visibility = View.GONE
        }

        setNavigationCardsSave()
        finishLoading()
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        if (binding.checkBoxAvailable.isChecked) {
            map[binding.editTextMinimumPrice] = R.string.minimum_order_price
            map[binding.editTextExtraFee] = R.string.extra_delivery_fee
            if (binding.checkBoxFreeThreshold.isChecked) {
                map[binding.editTextFreeThreshold] = R.string.free_delivery_threshold
            }
        }
        return map
    }

    override fun getDataObject(): SplitDataObject {
        itemId = itemId.ifEmpty { StringFormatUtils.formatId() }

        val basic = if (!binding.checkBoxAvailable.isChecked) {
            DeliveryBasic(available = false)
        } else {
            DeliveryBasic(
                available = true,
                minimumPrice = binding.editTextMinimumPrice.text.toString(),
                extraDeliveryFee = binding.editTextExtraFee.text.toString(),
                freeDeliveryAvailable = binding.checkBoxFreeThreshold.isChecked,
                minimumPriceFreeDelivery = binding.editTextFreeThreshold.text.ifEmpty { "0.0" }.toString()
            )
        }

        return SplitDataObject(itemId, basic, DeliveryDetails())
    }

    private fun initializeCheckBoxListeners() {
        binding.checkBoxAvailable.setOnCheckedChangeListener { _, isChecked ->
            binding.linearLayoutAvailableDelivery.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        binding.checkBoxFreeThreshold.setOnCheckedChangeListener { _, isChecked ->
            binding.editTextFreeThreshold.isEnabled = isChecked
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}