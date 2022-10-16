package pi.restaurant.management.ui.fragments.orders

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.databinding.FragmentCustomizeDishBinding
import pi.restaurant.management.databinding.ToolbarNavigationPreviewBinding
import pi.restaurant.management.model.activities.OrdersViewModel
import pi.restaurant.management.model.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.model.fragments.orders.CustomizeDishViewModel
import pi.restaurant.management.objects.data.dish.Dish
import pi.restaurant.management.objects.data.dish.DishItem
import pi.restaurant.management.objects.data.ingredient.IngredientItem
import pi.restaurant.management.objects.enums.DishType
import pi.restaurant.management.objects.enums.IngredientStatus
import pi.restaurant.management.ui.adapters.DishAllergensRecyclerAdapter
import pi.restaurant.management.ui.adapters.DishIngredientsRecyclerAdapter
import pi.restaurant.management.ui.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.ui.views.CustomNumberPicker
import pi.restaurant.management.utils.StringFormatUtils
import pi.restaurant.management.utils.UserInterfaceUtils
import java.math.BigDecimal


class CustomizeDishFragment : AbstractPreviewItemFragment() {
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation: ToolbarNavigationPreviewBinding get() = binding.toolbarNavigation
    override val editActionId = 0
    override val backActionId = 0
    override val viewModel : AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel : CustomizeDishViewModel by viewModels()

    private val activityViewModel : OrdersViewModel by activityViewModels()
    private var _binding: FragmentCustomizeDishBinding? = null
    val binding get() = _binding!!

    var otherIngredientsList: MutableList<IngredientItem> = ArrayList()
    var possibleIngredientsList: MutableList<IngredientItem> = ArrayList()

    lateinit var dish: Dish
    lateinit var numberPickerPortions: CustomNumberPicker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomizeDishBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    // TODO Edycja dania
    override fun initializeUI() {
        toolbarNavigation.root.visibility = View.VISIBLE
        toolbarNavigation.cardBack.root.visibility = View.GONE
        toolbarNavigation.cardAdd.root.visibility = View.VISIBLE

        toolbarNavigation.cardAdd.root.setOnClickListener {
            val dataObject = getDataObject()
            activityViewModel.savedOrder.value?.details?.dishes?.put(dataObject.id, dataObject)
            findNavController().navigate(activityViewModel.actionSave.value!!)
        }
    }

    override fun initializeWorkerUI() {
        toolbarNavigation.root.visibility = View.VISIBLE
        toolbarNavigation.cardBack.root.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun fillInData() {
        val item = _viewModel.item.value ?: Dish()
        dish = item
        if (!dish.basic.isActive) {
            Toast.makeText(requireContext(), "TODO This dish is inactive", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        numberPickerPortions = CustomNumberPicker(binding.numberPickerPortions, 1, 10, 1) { refreshTotalDishPrice() }

        binding.textViewName.text = item.basic.name
        if (item.basic.isDiscounted) {
            binding.textViewPrice.text = StringFormatUtils.formatPrice(item.basic.discountPrice)
            binding.textViewOriginalPrice.paintFlags =
                binding.textViewOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.textViewOriginalPrice.visibility = View.VISIBLE
            binding.textViewOriginalPrice.text = StringFormatUtils.formatPrice(item.basic.basePrice)
        } else {
            binding.textViewPrice.text = StringFormatUtils.formatPrice(item.basic.basePrice)
        }
        binding.textViewDishType.text = DishType.getString(item.basic.dishType, requireContext())
        binding.textViewAmount.text =
            StringFormatUtils.formatAmountWithUnit(requireContext(), item.details.amount, item.details.unit)
        refreshTotalDishPrice()

        otherIngredientsList = item.details.otherIngredients.toList().map { it.second }.toMutableList()
        possibleIngredientsList = item.details.possibleIngredients.toList().map { it.second }.toMutableList()

        initializeRecyclerViews(item)
        initializeMoreLessButtons()

        viewModel.setReadyToUnlock()
    }

    private fun initializeRecyclerViews(item: Dish) {
        binding.recyclerViewBaseIngredients.adapter =
            DishIngredientsRecyclerAdapter(item.details.baseIngredients.toList().map { it.second }.toMutableList(), this, IngredientStatus.BASE)
        UserInterfaceUtils.setRecyclerSize(binding.recyclerViewBaseIngredients, item.details.baseIngredients.size, requireContext())

        binding.recyclerViewOtherIngredients.adapter =
            DishIngredientsRecyclerAdapter(otherIngredientsList, this, IngredientStatus.OTHER)
        UserInterfaceUtils.setRecyclerSize(binding.recyclerViewOtherIngredients, item.details.otherIngredients.size, requireContext())

        binding.recyclerViewPossibleIngredients.adapter =
            DishIngredientsRecyclerAdapter(possibleIngredientsList, this, IngredientStatus.POSSIBLE)
        UserInterfaceUtils.setRecyclerSize(binding.recyclerViewPossibleIngredients, item.details.possibleIngredients.size, requireContext())

        binding.recyclerViewAllergens.adapter =
            DishAllergensRecyclerAdapter(item.details.allergens.toList().map { it.second }.toMutableList(), this)
        UserInterfaceUtils.setRecyclerSize(binding.recyclerViewAllergens, item.details.allergens.size, requireContext())
    }

    fun changeIngredientItemState(state: IngredientStatus, ingredientItem: IngredientItem) {
        if (state == IngredientStatus.OTHER) {
            removeIngredient(otherIngredientsList, binding.recyclerViewOtherIngredients, ingredientItem)
            addIngredient(possibleIngredientsList, binding.recyclerViewPossibleIngredients, ingredientItem)
        } else {
            removeIngredient(possibleIngredientsList, binding.recyclerViewPossibleIngredients, ingredientItem)
            addIngredient(otherIngredientsList, binding.recyclerViewOtherIngredients, ingredientItem)
        }
        refreshTotalDishPrice()
    }

    private fun refreshTotalDishPrice() {
        binding.textViewTotalDishPrice.text = StringFormatUtils.formatPrice(getFinalPrice())
    }

    private fun removeIngredient(list: MutableList<IngredientItem>, recycler: RecyclerView, item: IngredientItem) {
        val itemPosition = list.indexOf(item)
        list.remove(item)
        recycler.adapter?.notifyItemRemoved(itemPosition)
        UserInterfaceUtils.setRecyclerSize(recycler, list.size, requireContext())
    }

    private fun addIngredient(list: MutableList<IngredientItem>, recycler: RecyclerView, item: IngredientItem) {
        list.add(item)
        recycler.adapter?.notifyItemInserted(list.indexOf(item))
        UserInterfaceUtils.setRecyclerSize(recycler, list.size, requireContext())
    }

    private fun getDataObject(): DishItem {
        return DishItem(
            id = StringFormatUtils.formatId(),
            dish = dish,
            amount = numberPickerPortions.getValue(),
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

    private fun getFinalPrice() : String {
        var price = BigDecimal(if (dish.basic.isDiscounted) dish.basic.discountPrice else dish.basic.basePrice)
        price += otherIngredientsList.sumOf { BigDecimal(it.extraPrice) }
        return (price * BigDecimal(numberPickerPortions.getValue())).toString()
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
    }

}