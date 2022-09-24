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
import pi.restaurant.management.data.Dish
import pi.restaurant.management.data.DishItem
import pi.restaurant.management.data.IngredientItem
import pi.restaurant.management.databinding.FragmentPreviewDishBinding
import pi.restaurant.management.enums.DishType
import pi.restaurant.management.enums.IngredientItemState
import pi.restaurant.management.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.utils.StringFormatUtils
import pi.restaurant.management.utils.SubItemUtils


class CustomizeDishFragment : AbstractPreviewItemFragment() {
    override val linearLayout get() = binding.linearLayout
    override val editButton: Button? = null
    override val editActionId = R.id.actionPreviewDishToEditDish
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

    override fun fillInData(dataSnapshot: DataSnapshot) {
        binding.checkBoxActive.visibility = View.GONE

        val item = dataSnapshot.getValue<Dish>() ?: return
        dish = item

        binding.textViewName.text = item.name
        binding.textViewDescription.text = item.description
        binding.checkBoxActive.isChecked = item.isActive
        binding.textViewBasePrice.text = StringFormatUtils.formatPrice(item.basePrice)
        binding.checkBoxDiscount.isChecked = item.isDiscounted
        binding.textViewDiscountPrice.text = StringFormatUtils.formatPrice(item.discountPrice)
        binding.textViewDishType.text = DishType.getString(item.dishType, requireContext())
        binding.textViewAmountWithUnit.text =
            StringFormatUtils.formatAmountWithUnit(requireContext(), item.amount, item.unit)
        binding.textViewFinalPrice.text = StringFormatUtils.formatPrice(getFinalPrice())

        otherIngredientsList = item.otherIngredients.toList().map { it.second }.toMutableList()
        possibleIngredientsList = item.possibleIngredients.toList().map { it.second }.toMutableList()

        initializeRecyclerViews(item)

        binding.progress.progressBar.visibility = View.GONE
    }

    private fun initializeRecyclerViews(item: Dish) {
        binding.recyclerViewBaseIngredients.adapter =
            DishIngredientsRecyclerAdapter(item.baseIngredients.toList().map { it.second }.toMutableList(), this, 0)
        SubItemUtils.setRecyclerSize(binding.recyclerViewBaseIngredients, item.baseIngredients.size, requireContext())

        binding.recyclerViewOtherIngredients.adapter =
            DishIngredientsRecyclerAdapter(otherIngredientsList, this, 1)
        SubItemUtils.setRecyclerSize(binding.recyclerViewOtherIngredients, item.otherIngredients.size, requireContext())

        binding.recyclerViewPossibleIngredients.adapter =
            DishIngredientsRecyclerAdapter(possibleIngredientsList, this, 2)
        SubItemUtils.setRecyclerSize(binding.recyclerViewPossibleIngredients, item.possibleIngredients.size, requireContext())

        binding.recyclerViewAllergens.adapter =
            DishAllergensRecyclerAdapter(item.allergens.toList().map { it.second }.toMutableList(), this)
        SubItemUtils.setRecyclerSize(binding.recyclerViewAllergens, item.allergens.size, requireContext())
    }

    fun changeIngredientItemState(state: IngredientItemState, ingredientItem: IngredientItem) {
        if (state == IngredientItemState.POSSIBLE) {
            SubItemUtils.removeIngredientItem(otherIngredientsList, binding.recyclerViewOtherIngredients, ingredientItem, requireContext())
            SubItemUtils.addIngredientItem(possibleIngredientsList, binding.recyclerViewPossibleIngredients, ingredientItem, requireContext())
        } else {
            SubItemUtils.removeIngredientItem(possibleIngredientsList, binding.recyclerViewPossibleIngredients, ingredientItem, requireContext())
            SubItemUtils.addIngredientItem(otherIngredientsList, binding.recyclerViewOtherIngredients, ingredientItem, requireContext())
        }
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
            if (dish.otherIngredients.containsKey(ingredientItem.id)) {
                list.add(ingredientItem)
            }
        }
        return list
    }

    private fun getUsedPossibleIngredients() : ArrayList<IngredientItem> {
        val list = ArrayList<IngredientItem>()
        for (ingredientItem in otherIngredientsList) {
            if (dish.possibleIngredients.containsKey(ingredientItem.id)) {
                list.add(ingredientItem)
            }
        }
        return list
    }

    private fun getFinalPrice() : Double {
        var price = if (dish.isDiscounted) dish.discountPrice else dish.basePrice
        price += otherIngredientsList.sumOf { it.extraPrice }
        return price * binding.editTextPortions.text.toString().toInt()
    }

}