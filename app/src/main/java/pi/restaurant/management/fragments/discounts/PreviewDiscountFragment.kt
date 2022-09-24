package pi.restaurant.management.fragments.discounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.DiscountGroup
import pi.restaurant.management.databinding.FragmentPreviewDiscountBinding
import pi.restaurant.management.enums.DiscountType
import pi.restaurant.management.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.utils.StringFormatUtils

class PreviewDiscountFragment : AbstractPreviewItemFragment() {
    override val linearLayout get() = binding.linearLayout
    override val editButton get() = binding.buttonEdit
    override val editActionId = R.id.actionPreviewDiscountToEditDiscount
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

    override fun fillInData(dataSnapshot: DataSnapshot) {
        val item = dataSnapshot.getValue<DiscountGroup>() ?: return

        binding.textViewAvailable.text = item.availableDiscounts.size.toString()
        binding.textViewAssigned.text = item.assignedDiscounts.size.toString()
        binding.textViewUsed.text = item.usedDiscounts.size.toString()
        binding.textViewCode.text = item.id
        binding.textViewAmount.text = item.amount.toString()
        binding.textViewType.text = DiscountType.getString(item.type, requireContext())
        binding.textViewExpirationDate.text = StringFormatUtils.formatDate(item.expirationDate)
        binding.progress.progressBar.visibility = View.GONE
    }
}