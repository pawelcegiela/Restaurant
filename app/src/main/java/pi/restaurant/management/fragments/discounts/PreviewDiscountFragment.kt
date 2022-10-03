package pi.restaurant.management.fragments.discounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.*
import pi.restaurant.management.databinding.FragmentPreviewDiscountBinding
import pi.restaurant.management.enums.DiscountType
import pi.restaurant.management.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.utils.SnapshotsPair
import pi.restaurant.management.utils.StringFormatUtils

class PreviewDiscountFragment : AbstractPreviewItemFragment() {
    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val cardSetNavigation get() = binding.cardSetNavigation
    override val editActionId = R.id.actionPreviewDiscountToEditDiscount
    override val backActionId = R.id.actionPreviewDiscountToDiscounts
    override val viewModel : AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel : PreviewDiscountViewModel by viewModels()

    private var _binding: FragmentPreviewDiscountBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewDiscountBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getItem(snapshotsPair: SnapshotsPair) : Discount {
        val basic = snapshotsPair.basic?.getValue<DiscountBasic>() ?: DiscountBasic()
        val details = snapshotsPair.details?.getValue<DiscountDetails>() ?: DiscountDetails()
        return Discount(itemId, basic, details)
    }

    override fun fillInData(snapshotsPair: SnapshotsPair) {
        val item = getItem(snapshotsPair)

        binding.textViewAvailable.text = item.basic.availableDiscounts.size.toString()
        binding.textViewAssigned.text = item.basic.assignedDiscounts.size.toString()
        binding.textViewUsed.text = item.basic.usedDiscounts.size.toString()
        binding.textViewCode.text = item.basic.id
        binding.textViewAmount.text = item.basic.amount.toString()
        binding.textViewType.text = DiscountType.getString(item.basic.type, requireContext())
        binding.textViewExpirationDate.text = StringFormatUtils.formatDate(item.basic.expirationDate)

        viewModel.liveReadyToUnlock.value = true
    }
}