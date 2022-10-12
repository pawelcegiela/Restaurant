package pi.restaurant.management.ui.fragments.dishes

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.ui.adapters.DishAllergensRecyclerAdapter
import pi.restaurant.management.ui.adapters.PreviewDishIngredientRecyclerAdapter
import pi.restaurant.management.objects.data.dish.Dish
import pi.restaurant.management.objects.data.ingredient.IngredientItem
import pi.restaurant.management.databinding.FragmentPreviewDishBinding
import pi.restaurant.management.databinding.ToolbarNavigationPreviewBinding
import pi.restaurant.management.objects.enums.DishType
import pi.restaurant.management.objects.enums.IngredientStatus
import pi.restaurant.management.ui.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.model.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.model.fragments.dishes.PreviewDishViewModel
import pi.restaurant.management.utils.StringFormatUtils
import pi.restaurant.management.utils.UserInterfaceUtils

class PreviewDishFragment : AbstractPreviewItemFragment() {
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation: ToolbarNavigationPreviewBinding get() = binding.toolbarNavigation
    override val editActionId = R.id.actionPreviewDishToEditDish
    override val backActionId = R.id.actionPreviewDishToDishes
    override val viewModel: AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel: PreviewDishViewModel by viewModels()

    private var _binding: FragmentPreviewDishBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewDishBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun fillInData() {
        val item = _viewModel.item.value ?: Dish()

        if (!item.basic.isActive) {
            binding.cardViewDisabled.root.visibility = View.VISIBLE
            binding.cardViewDisabled.textViewInfo.text = getText(R.string.dish_unavailable)
        }
        if (item.basic.isDiscounted) {
            binding.textViewOriginalPrice.paintFlags =
                binding.textViewOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        binding.textViewDishType.text = DishType.getString(item.basic.dishType, requireContext())
        binding.textViewAmount.text =
            StringFormatUtils.formatAmountWithUnit(requireContext(), item.details.amount, item.details.unit)

        if (item.details.possibleIngredients.isEmpty()) {
            binding.textViewPossibleIngredients.visibility = View.GONE
        }
        if (item.details.allergens.isEmpty()) {
            binding.cardAllergens.visibility = View.GONE
        }

        initializeRecyclerViews(item)
        initializeMoreLessButtons()

        viewModel.setReadyToUnlock()
    }

    private fun initializeRecyclerViews(item: Dish) {
        val baseOtherIngredients = ArrayList<Pair<IngredientItem, IngredientStatus>>()
        baseOtherIngredients.addAll(item.details.baseIngredients.toList().map { it.second to IngredientStatus.BASE })
        baseOtherIngredients.addAll(item.details.otherIngredients.toList().map { it.second to IngredientStatus.OTHER })

        binding.recyclerViewBaseOtherIngredients.adapter =
            PreviewDishIngredientRecyclerAdapter(baseOtherIngredients, this)
        UserInterfaceUtils.setRecyclerSize(binding.recyclerViewBaseOtherIngredients, baseOtherIngredients.size, requireContext())

        val possibleIngredients = item.details.possibleIngredients.toList().map { it.second to IngredientStatus.POSSIBLE }

        binding.recyclerViewPossibleIngredients.adapter =
            PreviewDishIngredientRecyclerAdapter(possibleIngredients, this)
        UserInterfaceUtils.setRecyclerSize(binding.recyclerViewPossibleIngredients, possibleIngredients.size, requireContext())

        binding.recyclerViewAllergens.adapter =
            DishAllergensRecyclerAdapter(item.details.allergens.toList().map { it.second }.toMutableList(), this)
        UserInterfaceUtils.setRecyclerSize(binding.recyclerViewAllergens, item.details.allergens.size, requireContext())
    }

    private fun initializeMoreLessButtons() {
        binding.textViewDetailsMore.setOnClickListener {
            binding.linearLayoutDetailsUnexpanded.visibility = View.GONE
            binding.linearLayoutDetailsExpanded.visibility = View.VISIBLE
        }

        binding.textViewDetailsLess.setOnClickListener {
            binding.linearLayoutDetailsUnexpanded.visibility = View.VISIBLE
            binding.linearLayoutDetailsExpanded.visibility = View.GONE
        }

        binding.textViewIngredientsMore.setOnClickListener {
            binding.linearLayoutIngredientsUnexpanded.visibility = View.GONE
            binding.linearLayoutIngredientsExpanded.visibility = View.VISIBLE
        }

        binding.textViewIngredientsLess.setOnClickListener {
            binding.linearLayoutIngredientsUnexpanded.visibility = View.VISIBLE
            binding.linearLayoutIngredientsExpanded.visibility = View.GONE
        }
    }
}