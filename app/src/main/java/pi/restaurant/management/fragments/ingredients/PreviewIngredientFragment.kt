package pi.restaurant.management.fragments.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.adapters.ContainingItemsRecyclerAdapter
import pi.restaurant.management.adapters.DishIngredientsRecyclerAdapter
import pi.restaurant.management.adapters.DishesRecyclerAdapter
import pi.restaurant.management.data.*
import pi.restaurant.management.databinding.FragmentPreviewIngredientBinding
import pi.restaurant.management.enums.IngredientStatus
import pi.restaurant.management.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.utils.SnapshotsPair
import pi.restaurant.management.utils.StringFormatUtils
import pi.restaurant.management.utils.SubItemUtils

class PreviewIngredientFragment : AbstractPreviewItemFragment() {
    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val cardSetNavigation get() = binding.cardSetNavigation
    override val editActionId = R.id.actionPreviewIngredientToEditIngredient
    override val backActionId = R.id.actionPreviewIngredientToIngredients
    override val viewModel: AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel: PreviewIngredientViewModel by viewModels()

    private var _binding: FragmentPreviewIngredientBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewIngredientBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getItem(snapshotsPair: SnapshotsPair): Ingredient {
        val basic = snapshotsPair.basic?.getValue<IngredientBasic>() ?: IngredientBasic()
        val details = snapshotsPair.details?.getValue<IngredientDetails>() ?: IngredientDetails()
        return Ingredient(itemId, basic, details)
    }

    override fun fillInData(snapshotsPair: SnapshotsPair) {
        val item = getItem(snapshotsPair)

        binding.textViewName.text = item.basic.name

        if (item.basic.subDish) {
            binding.textViewAmountTitle.visibility = View.GONE
            binding.textViewAmount.visibility = View.GONE
        } else {
            binding.textViewAmount.text =
                StringFormatUtils.formatAmountWithUnit(requireContext(), item.basic.amount, item.basic.unit)
        }

        binding.textViewType.text = if (item.basic.subDish) getString(R.string.sub_dish) else getString(R.string.ingredient)

        if (item.basic.subDish && item.details.subIngredients != null) {
            binding.recyclerViewSubIngredients.adapter =
                DishIngredientsRecyclerAdapter(item.details.subIngredients!!, this, IngredientStatus.BASE)
            SubItemUtils.setRecyclerSize(binding.recyclerViewSubIngredients, item.details.subIngredients!!.size, requireContext())
        } else {
            binding.cardSubIngredients.visibility = View.GONE
        }

        if (item.details.containingDishes.isEmpty() && item.details.containingSubDishes.isEmpty()) {
            binding.cardContaining.visibility = View.GONE
            viewModel.liveReadyToUnlock.value = true
        } else {
            setContainingDishes(item.details)
            setContainingSubDishes(item.details)
        }
    }

    private fun setContainingDishes(details: IngredientDetails) {
        val containingDishesIds = details.containingDishes.map { it.key }
        if (containingDishesIds.isEmpty()) {
            binding.textViewContainingDishes.visibility = View.GONE
            binding.recyclerViewDishesContaining.visibility = View.GONE
        } else {
            _viewModel.getContainingDishes(containingDishesIds)

            _viewModel.liveContainingDishes.observe(viewLifecycleOwner) { containingDishes ->
                if (containingDishes.size == containingDishesIds.size) {
                    binding.recyclerViewDishesContaining.adapter =
                        ContainingItemsRecyclerAdapter(containingDishes, this)
                    SubItemUtils.setRecyclerSize(binding.recyclerViewDishesContaining, containingDishes.size, requireContext())

                    if (details.containingSubDishes.size == _viewModel.containingSubDishes.size) {
                        viewModel.liveReadyToUnlock.value = true
                    }
                }
            }
        }
    }

    private fun setContainingSubDishes(details: IngredientDetails) {
        val containingSubDishesIds = details.containingSubDishes.map { it.key }
        if (containingSubDishesIds.isEmpty()) {
            binding.textViewContainingSubDishes.visibility = View.GONE
            binding.recyclerViewSubDishesContaining.visibility = View.GONE
        } else {
            _viewModel.getContainingSubDishes(containingSubDishesIds)

            _viewModel.liveContainingSubDishes.observe(viewLifecycleOwner) { containingSubDishes ->
                if (containingSubDishes.size == containingSubDishesIds.size) {
                    binding.recyclerViewSubDishesContaining.adapter =
                        ContainingItemsRecyclerAdapter(containingSubDishes, this)
                    SubItemUtils.setRecyclerSize(
                        binding.recyclerViewSubDishesContaining,
                        containingSubDishes.size,
                        requireContext()
                    )

                    if (details.containingDishes.size == _viewModel.containingDishes.size) {
                        viewModel.liveReadyToUnlock.value = true
                    }
                }
            }
        }
    }
}