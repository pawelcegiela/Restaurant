package pi.restaurant.management.ui.fragments.dishes

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.ui.adapters.DishAllergensRecyclerAdapter
import pi.restaurant.management.ui.adapters.PreviewDishIngredientRecyclerAdapter
import pi.restaurant.management.objects.data.dish.Dish
import pi.restaurant.management.objects.data.dish.DishBasic
import pi.restaurant.management.objects.data.dish.DishDetails
import pi.restaurant.management.objects.data.ingredient.IngredientItem
import pi.restaurant.management.databinding.FragmentPreviewDishBinding
import pi.restaurant.management.objects.enums.DishType
import pi.restaurant.management.objects.enums.IngredientStatus
import pi.restaurant.management.ui.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.logic.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.logic.fragments.dishes.PreviewDishViewModel
import pi.restaurant.management.objects.SnapshotsPair
import pi.restaurant.management.utils.StringFormatUtils
import pi.restaurant.management.utils.UserInterfaceUtils

class PreviewDishFragment : AbstractPreviewItemFragment() {
    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val cardSetNavigation get() = binding.cardSetNavigation
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
        return binding.root
    }

    private fun getItem(snapshotsPair: SnapshotsPair): Dish {
        val basic = snapshotsPair.basic?.getValue<DishBasic>() ?: DishBasic()
        val details = snapshotsPair.details?.getValue<DishDetails>() ?: DishDetails()
        return Dish(itemId, basic, details)
    }

    override fun fillInData(snapshotsPair: SnapshotsPair) {
        val item = getItem(snapshotsPair)

        binding.textViewName.text = item.basic.name
        if (item.details.description.isNotEmpty()) {
            binding.textViewDescription.text = item.details.description
        }
        if (item.details.recipe.isNotEmpty()) {
            binding.textViewRecipe.text = item.details.recipe
        }
        if (!item.basic.isActive) {
            binding.cardViewDisabled.root.visibility = View.VISIBLE
            binding.cardViewDisabled.textViewInfo.text = getText(R.string.dish_unavailable)
        }
        if (item.basic.isDiscounted) {
            binding.textViewPrice.text = StringFormatUtils.formatPrice(item.basic.discountPrice)
            binding.textViewOriginalPrice.paintFlags =
                binding.textViewFinalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.textViewOriginalPrice.visibility = View.VISIBLE
            binding.textViewOriginalPrice.text = StringFormatUtils.formatPrice(item.basic.basePrice)
        } else {
            binding.textViewPrice.text = StringFormatUtils.formatPrice(item.basic.basePrice)
        }
        binding.textViewDishType.text = DishType.getString(item.basic.dishType, requireContext())
        binding.textViewAmount.text =
            StringFormatUtils.formatAmountWithUnit(requireContext(), item.details.amount, item.details.unit)

        initializeRecyclerViews(item)
        initializeMoreLessButtons()

        viewModel.liveReadyToUnlock.value = true
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