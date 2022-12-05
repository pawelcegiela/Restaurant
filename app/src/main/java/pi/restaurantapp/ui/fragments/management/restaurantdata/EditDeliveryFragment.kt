package pi.restaurantapp.ui.fragments.management.restaurantdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentModifyDeliveryBinding
import pi.restaurantapp.ui.fragments.AbstractModifyItemFragment
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.restaurantdata.EditDeliveryViewModel

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

    override val viewModel: AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel: EditDeliveryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyDeliveryBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.fragment = this
        binding.lifecycleOwner = this
        binding.linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun initializeUI() {
        initializeCheckBoxListeners()
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