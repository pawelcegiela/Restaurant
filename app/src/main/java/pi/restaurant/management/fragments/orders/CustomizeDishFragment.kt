package pi.restaurant.management.fragments.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.adapters.DishAllergensRecyclerAdapter
import pi.restaurant.management.adapters.DishIngredientsRecyclerAdapter
import pi.restaurant.management.data.*
import pi.restaurant.management.databinding.CardSetEditBackBinding
import pi.restaurant.management.databinding.FragmentPreviewDishBinding
import pi.restaurant.management.enums.DishType
import pi.restaurant.management.enums.IngredientItemState
import pi.restaurant.management.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.utils.SnapshotsPair
import pi.restaurant.management.utils.StringFormatUtils
import pi.restaurant.management.utils.SubItemUtils


class CustomizeDishFragment : AbstractPreviewItemFragment() {
    override val linearLayout get() = binding.linearLayout
    override val cardSetNavigation: CardSetEditBackBinding? = null
    override val editActionId = R.id.actionPreviewDishToEditDish
    override val backActionId = R.id.actionPreviewOrderToOrders // TODO czy ok?
    override val viewModel : AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel : CustomizeDishViewModel by viewModels()

    private var _binding: FragmentPreviewDishBinding? = null
    val binding get() = _binding!!

    var otherIngredientsList: MutableList<IngredientItem> = ArrayList()
    var possibleIngredientsList: MutableList<IngredientItem> = ArrayList()

    lateinit var dish: Dish

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewDishBinding.inflate(inflater, container, false)
        return binding.root
    }

    // TODO Edycja dania
    override fun initializeUI() {
        binding.buttonAdd.visibility = View.VISIBLE
        binding.editTextPortions.visibility = View.VISIBLE
        binding.textViewFinalPrice.visibility = View.VISIBLE
        binding.buttonAdd.setOnClickListener {
            val navController = findNavController()
            navController.previousBackStackEntry?.savedStateHandle?.set("newItem", getDataObject())
            navController.popBackStack()
        }
    }

    private fun getItem(snapshotsPair: SnapshotsPair) : Dish {
        val basic = snapshotsPair.basic?.getValue<DishBasic>() ?: DishBasic()
        val details = snapshotsPair.details?.getValue<DishDetails>() ?: DishDetails()
        return Dish(itemId, basic, details)
    }

    override fun fillInData(snapshotsPair: SnapshotsPair) {
//        binding.checkBoxActive.visibility = View.GONE TODO Ukryj nieaktywne

        val item = getItem(snapshotsPair)
        dish = item

        // TODO Wszystko zrobić jak w PreviewDish

        binding.textViewName.text = item.basic.name
        binding.textViewDescription.text = item.details.description
//        binding.checkBoxActive.isChecked = item.basic.isActive
//        binding.textViewBasePrice.text = StringFormatUtils.formatPrice(item.basic.basePrice)
//        binding.checkBoxDiscount.isChecked = item.basic.isDiscounted
//        binding.textViewDiscountPrice.text = StringFormatUtils.formatPrice(item.basic.discountPrice)
        binding.textViewDishType.text = DishType.getString(item.basic.dishType, requireContext())
        binding.textViewAmount.text =
            StringFormatUtils.formatAmountWithUnit(requireContext(), item.details.amount, item.details.unit)
        binding.textViewFinalPrice.text = StringFormatUtils.formatPrice(getFinalPrice())

        otherIngredientsList = item.details.otherIngredients.toList().map { it.second }.toMutableList()
        possibleIngredientsList = item.details.possibleIngredients.toList().map { it.second }.toMutableList()

        initializeRecyclerViews(item)

        binding.progress.progressBar.visibility = View.GONE
    }

    private fun initializeRecyclerViews(item: Dish) {
//        binding.recyclerViewBaseIngredients.adapter =
//            DishIngredientsRecyclerAdapter(item.details.baseIngredients.toList().map { it.second }.toMutableList(), this, 0)
//        SubItemUtils.setRecyclerSize(binding.recyclerViewBaseIngredients, item.details.baseIngredients.size, requireContext())
//
//        binding.recyclerViewOtherIngredients.adapter =
//            DishIngredientsRecyclerAdapter(otherIngredientsList, this, 1)
//        SubItemUtils.setRecyclerSize(binding.recyclerViewOtherIngredients, item.details.otherIngredients.size, requireContext())

        binding.recyclerViewPossibleIngredients.adapter =
            DishIngredientsRecyclerAdapter(possibleIngredientsList, this, 2)
        SubItemUtils.setRecyclerSize(binding.recyclerViewPossibleIngredients, item.details.possibleIngredients.size, requireContext())

        binding.recyclerViewAllergens.adapter =
            DishAllergensRecyclerAdapter(item.details.allergens.toList().map { it.second }.toMutableList(), this)
        SubItemUtils.setRecyclerSize(binding.recyclerViewAllergens, item.details.allergens.size, requireContext())
    }

    fun changeIngredientItemState(state: IngredientItemState, ingredientItem: IngredientItem) {
//        if (state == IngredientItemState.POSSIBLE) {
//            SubItemUtils.removeIngredientItem(otherIngredientsList, binding.recyclerViewOtherIngredients, ingredientItem, requireContext())
//            SubItemUtils.addIngredientItem(possibleIngredientsList, binding.recyclerViewPossibleIngredients, ingredientItem, requireContext())
//        } else {
//            SubItemUtils.removeIngredientItem(possibleIngredientsList, binding.recyclerViewPossibleIngredients, ingredientItem, requireContext())
//            SubItemUtils.addIngredientItem(otherIngredientsList, binding.recyclerViewOtherIngredients, ingredientItem, requireContext())
//        }
        binding.textViewFinalPrice.text = StringFormatUtils.formatPrice(getFinalPrice())
    }

    private fun getDataObject(): DishItem {
        return DishItem(
            id = itemId,
            dish = dish,
            amount = binding.editTextPortions.text.toString().toInt(),
            unusedOtherIngredients = getUnusedOtherIngredients(),
            usedPossibleIngredients = getUsedPossibleIngredients(),
            finalPrice = getFinalPrice()
        )
    }

    private fun getUnusedOtherIngredients() : ArrayList<IngredientItem> {
        val list = ArrayList<IngredientItem>()
        for (ingredientItem in possibleIngredientsList) {
            if (dish.details.otherIngredients.containsKey(ingredientItem.id)) {
                list.add(ingredientItem)
            }
        }
        return list
    }

    private fun getUsedPossibleIngredients() : ArrayList<IngredientItem> {
        val list = ArrayList<IngredientItem>()
        for (ingredientItem in otherIngredientsList) {
            if (dish.details.possibleIngredients.containsKey(ingredientItem.id)) {
                list.add(ingredientItem)
            }
        }
        return list
    }

    private fun getFinalPrice() : Double {
        var price = if (dish.basic.isDiscounted) dish.basic.discountPrice else dish.basic.basePrice
        price += otherIngredientsList.sumOf { it.extraPrice }
        return price * binding.editTextPortions.text.toString().toInt()
    }

}