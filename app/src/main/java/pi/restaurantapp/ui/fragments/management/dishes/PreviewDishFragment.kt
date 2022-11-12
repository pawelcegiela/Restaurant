package pi.restaurantapp.ui.fragments.management.dishes

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentPreviewDishBinding
import pi.restaurantapp.databinding.ToolbarNavigationPreviewBinding
import pi.restaurantapp.model.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.model.fragments.management.dishes.PreviewDishViewModel
import pi.restaurantapp.objects.data.dish.Dish
import pi.restaurantapp.objects.data.ingredient.IngredientItem
import pi.restaurantapp.objects.enums.IngredientStatus
import pi.restaurantapp.ui.RecyclerManager
import pi.restaurantapp.ui.adapters.DishAllergensRecyclerAdapter
import pi.restaurantapp.ui.adapters.PreviewDishIngredientRecyclerAdapter
import pi.restaurantapp.ui.fragments.AbstractPreviewItemFragment
import pi.restaurantapp.utils.StringFormatUtils
import pi.restaurantapp.utils.UserInterfaceUtils

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
            binding.textViewOriginalPrice.paintFlags = binding.textViewOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        binding.textViewPrice.text = StringFormatUtils.formatPrice(if (item.basic.isDiscounted) item.basic.discountPrice else item.basic.basePrice)
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
        binding.recyclerViewBaseOtherIngredients.layoutManager = RecyclerManager(context)
        UserInterfaceUtils.setRecyclerSize(binding.recyclerViewBaseOtherIngredients, baseOtherIngredients.size, requireContext())

        val possibleIngredients = item.details.possibleIngredients.toList().map { it.second to IngredientStatus.POSSIBLE }

        binding.recyclerViewPossibleIngredients.adapter =
            PreviewDishIngredientRecyclerAdapter(possibleIngredients, this)
        binding.recyclerViewPossibleIngredients.layoutManager = RecyclerManager(context)
        UserInterfaceUtils.setRecyclerSize(binding.recyclerViewPossibleIngredients, possibleIngredients.size, requireContext())

        binding.recyclerViewAllergens.adapter =
            DishAllergensRecyclerAdapter(item.details.allergens.toList().map { it.second }.toMutableList(), this)
        binding.recyclerViewAllergens.layoutManager = RecyclerManager(context)
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