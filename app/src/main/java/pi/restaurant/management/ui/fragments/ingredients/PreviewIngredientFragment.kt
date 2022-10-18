package pi.restaurant.management.ui.fragments.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.databinding.FragmentPreviewIngredientBinding
import pi.restaurant.management.databinding.ToolbarNavigationPreviewBinding
import pi.restaurant.management.model.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.model.fragments.ingredients.PreviewIngredientViewModel
import pi.restaurant.management.objects.data.ingredient.Ingredient
import pi.restaurant.management.objects.data.ingredient.IngredientDetails
import pi.restaurant.management.objects.enums.IngredientStatus
import pi.restaurant.management.ui.RecyclerManager
import pi.restaurant.management.ui.adapters.ContainingItemsRecyclerAdapter
import pi.restaurant.management.ui.adapters.DishIngredientsRecyclerAdapter
import pi.restaurant.management.ui.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.utils.StringFormatUtils
import pi.restaurant.management.utils.UserInterfaceUtils

class PreviewIngredientFragment : AbstractPreviewItemFragment() {
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation: ToolbarNavigationPreviewBinding get() = binding.toolbarNavigation
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
        binding.vm = _viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun fillInData() {
        val item = _viewModel.item.value ?: Ingredient()

        if (!item.basic.subDish) {
            binding.textViewAmount.text =
                StringFormatUtils.formatAmountWithUnit(requireContext(), item.basic.amount.toString(), item.basic.unit)
        }

        binding.textViewType.text = if (item.basic.subDish) getString(R.string.sub_dish) else getString(R.string.ingredient)

        if (item.basic.subDish && item.details.subIngredients != null) {
            binding.recyclerViewSubIngredients.adapter =
                DishIngredientsRecyclerAdapter(item.details.subIngredients!!, this, IngredientStatus.BASE)
            binding.recyclerViewSubIngredients.layoutManager = RecyclerManager(context)
            UserInterfaceUtils.setRecyclerSize(binding.recyclerViewSubIngredients, item.details.subIngredients!!.size, requireContext())
        } else {
            binding.cardSubIngredients.visibility = View.GONE
        }

        if (item.details.containingDishes.isEmpty() && item.details.containingSubDishes.isEmpty()) {
//            binding.cardContaining.visibility = View.GONE
            viewModel.setReadyToUnlock()
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
                    binding.recyclerViewDishesContaining.layoutManager = RecyclerManager(context)
                    UserInterfaceUtils.setRecyclerSize(binding.recyclerViewDishesContaining, containingDishes.size, requireContext())

                    if (details.containingSubDishes.size == _viewModel.containingSubDishes.size) {
                        viewModel.setReadyToUnlock()
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
                    binding.recyclerViewSubDishesContaining.layoutManager = RecyclerManager(context)
                    UserInterfaceUtils.setRecyclerSize(
                        binding.recyclerViewSubDishesContaining,
                        containingSubDishes.size,
                        requireContext()
                    )

                    if (details.containingDishes.size == _viewModel.containingDishes.size) {
                        viewModel.setReadyToUnlock()
                    }
                }
            }
        }
    }
}