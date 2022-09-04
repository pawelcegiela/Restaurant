package pi.restaurant.management.fragments.dishes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.data.Dish
import pi.restaurant.management.databinding.FragmentPreviewDishBinding
import pi.restaurant.management.enums.DishType
import pi.restaurant.management.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.utils.StringFormatUtils

class PreviewDishFragment : AbstractPreviewItemFragment() {
    override val databasePath = "dishes"
    override val linearLayout get() = binding.linearLayout

    private var _binding: FragmentPreviewDishBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewDishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun fillInData(dataSnapshot: DataSnapshot) {
        val item = dataSnapshot.getValue<Dish>() ?: return
        binding.textViewName.text = item.name
        binding.textViewDescription.text = item.description
        binding.checkBoxActive.isChecked = item.isActive
        binding.textViewBasePrice.text = "${item.basePrice} zł"
        binding.checkBoxDiscount.isChecked = item.isDiscounted
        binding.textViewDiscountPrice.text = "${item.discountPrice} zł"
        binding.textViewDishType.text = getString(DishType.getNameResById(item.dishType))
        binding.textViewAmountWithUnit.text =
            StringFormatUtils.formatAmountWithUnit(context!!, item.amount, item.unit)
        binding.progress.progressBar.visibility = View.GONE
    }
}