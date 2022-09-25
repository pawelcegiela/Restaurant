package pi.restaurant.management.fragments.dishes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.adapters.DishAllergensRecyclerAdapter
import pi.restaurant.management.adapters.DishIngredientsRecyclerAdapter
import pi.restaurant.management.data.Dish
import pi.restaurant.management.data.DishBasic
import pi.restaurant.management.data.DishDetails
import pi.restaurant.management.databinding.FragmentPreviewDishBinding
import pi.restaurant.management.enums.DishType
import pi.restaurant.management.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.utils.SnapshotsPair
import pi.restaurant.management.utils.StringFormatUtils
import pi.restaurant.management.utils.SubItemUtils

class PreviewDishFragment : AbstractPreviewItemFragment() {
    override val linearLayout get() = binding.linearLayout
    override val editButton get() = binding.buttonEdit
    override val editActionId = R.id.actionPreviewDishToEditDish
    override val viewModel : AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel : PreviewDishViewModel by viewModels()

    private var _binding: FragmentPreviewDishBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewDishBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getItem(snapshotsPair: SnapshotsPair) : Dish {
        val basic = snapshotsPair.basic?.getValue<DishBasic>() ?: DishBasic()
        val details = snapshotsPair.details?.getValue<DishDetails>() ?: DishDetails()
        return Dish(itemId, basic, details)
    }

    override fun fillInData(snapshotsPair: SnapshotsPair) {
        val item = getItem(snapshotsPair)

        binding.textViewName.text = item.basic.name
        binding.textViewDescription.text = item.details.description
        binding.checkBoxActive.isChecked = item.basic.isActive
        binding.textViewBasePrice.text = StringFormatUtils.formatPrice(item.basic.basePrice)
        binding.checkBoxDiscount.isChecked = item.basic.isDiscounted
        binding.textViewDiscountPrice.text = StringFormatUtils.formatPrice(item.basic.discountPrice)
        binding.textViewDishType.text = DishType.getString(item.basic.dishType, requireContext())
        binding.textViewAmountWithUnit.text =
            StringFormatUtils.formatAmountWithUnit(requireContext(), item.details.amount, item.details.unit)

        initializeRecyclerViews(item)

        binding.progress.progressBar.visibility = View.GONE
    }

    private fun initializeRecyclerViews(item: Dish) {
        binding.recyclerViewBaseIngredients.adapter =
            DishIngredientsRecyclerAdapter(item.details.baseIngredients.toList().map { it.second }.toMutableList(), this, 0)
        SubItemUtils.setRecyclerSize(binding.recyclerViewBaseIngredients, item.details.baseIngredients.size, requireContext())

        binding.recyclerViewOtherIngredients.adapter =
            DishIngredientsRecyclerAdapter(item.details.otherIngredients.toList().map { it.second }.toMutableList(), this, 1)
        SubItemUtils.setRecyclerSize(binding.recyclerViewOtherIngredients, item.details.otherIngredients.size, requireContext())

        binding.recyclerViewPossibleIngredients.adapter =
            DishIngredientsRecyclerAdapter(item.details.possibleIngredients.toList().map { it.second }.toMutableList(), this, 2)
        SubItemUtils.setRecyclerSize(binding.recyclerViewPossibleIngredients, item.details.possibleIngredients.size, requireContext())

        binding.recyclerViewAllergens.adapter =
            DishAllergensRecyclerAdapter(item.details.allergens.toList().map { it.second }.toMutableList(), this)
        SubItemUtils.setRecyclerSize(binding.recyclerViewAllergens, item.details.allergens.size, requireContext())
    }
}