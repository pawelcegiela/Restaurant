package pi.restaurantapp.ui.fragments.management.orders

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import pi.restaurantapp.databinding.FragmentCustomizeDishBinding
import pi.restaurantapp.databinding.ToolbarNavigationPreviewBinding
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.objects.data.ingredient.IngredientItem
import pi.restaurantapp.objects.enums.IngredientStatus
import pi.restaurantapp.ui.fragments.AbstractPreviewItemFragment
import pi.restaurantapp.ui.pickers.CustomNumberPicker
import pi.restaurantapp.viewmodels.activities.management.OrdersViewModel
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.orders.CustomizeDishViewModel

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for CustomizeDishFragment.
 * @see pi.restaurantapp.viewmodels.fragments.management.orders.CustomizeDishViewModel ViewModel layer
 * @see pi.restaurantapp.logic.fragments.management.orders.CustomizeDishLogic Model layer
 */
open class CustomizeDishFragment : AbstractPreviewItemFragment() {
    override val toolbarNavigation: ToolbarNavigationPreviewBinding get() = binding.toolbarNavigation
    override val editActionId = 0
    override val backActionId = 0
    override val viewModel: AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel: CustomizeDishViewModel by viewModels()

    protected open val activityViewModel: OrdersViewModel by activityViewModels()
    private var _binding: FragmentCustomizeDishBinding? = null
    val binding get() = _binding!!

    private lateinit var numberPickerPortions: CustomNumberPicker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomizeDishBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (activityViewModel.editedDish.value == null) {
            super.onViewCreated(view, savedInstanceState)
        } else {
            _viewModel.setItem(activityViewModel.editedDish.value ?: DishItem())
            viewModel.initializeData(activityViewModel.editedDish.value!!.id)
            activityViewModel.resetEditedDish()
            addLiveDataObservers()
        }
    }

    override fun initializeNavigationToolbar() {
        toolbarNavigation.cardAdd.root.setOnClickListener {
            val dataObject = getDataObject()
            activityViewModel.savedOrder.value?.details?.dishes?.put(dataObject.id, dataObject)
            findNavController().navigate(activityViewModel.actionSave.value!!)
        }
    }

    override fun initializeExtraData() {
        val item = _viewModel.item.value ?: DishItem()
        val dish = item.dish

        numberPickerPortions = CustomNumberPicker(binding.numberPickerPortions, 1, 10, 1) { value ->
            _viewModel.item.value?.amount = value
            refreshTotalDishPrice()
        }
        numberPickerPortions.setValue(item.amount)

        if (dish.basic.isDiscounted) {
            binding.textViewOriginalPrice.paintFlags = binding.textViewOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        refreshTotalDishPrice()
        initializeMoreLessButtons()

        viewModel.setReadyToUnlock()
    }

    fun changeIngredientItemState(state: IngredientStatus, ingredientItem: IngredientItem) {
        if (state == IngredientStatus.OTHER) {
            removeIngredient(_viewModel.observer.otherIngredients, binding.recyclerViewOtherIngredients, ingredientItem)
            addIngredient(_viewModel.observer.possibleIngredients, binding.recyclerViewPossibleIngredients, ingredientItem)
        } else {
            removeIngredient(_viewModel.observer.possibleIngredients, binding.recyclerViewPossibleIngredients, ingredientItem)
            addIngredient(_viewModel.observer.otherIngredients, binding.recyclerViewOtherIngredients, ingredientItem)
        }
        refreshTotalDishPrice()
    }

    private fun refreshTotalDishPrice() {
        binding.textViewTotalDishPrice.text = StringFormatUtils.formatPrice(_viewModel.getFinalPrice())
    }

    private fun removeIngredient(list: MutableList<IngredientItem>, recycler: RecyclerView, item: IngredientItem) {
        val itemPosition = list.indexOf(item)
        list.remove(item)
        recycler.adapter?.notifyItemRemoved(itemPosition)
    }

    private fun addIngredient(list: MutableList<IngredientItem>, recycler: RecyclerView, item: IngredientItem) {
        list.add(item)
        recycler.adapter?.notifyItemInserted(list.indexOf(item))
    }

    protected fun getDataObject(): DishItem {
        val dishItem = _viewModel.item.value!!

        return DishItem(
            id = viewModel.itemId,
            dish = dishItem.dish,
            amount = numberPickerPortions.getValue(),
            unusedOtherIngredients = getUnusedOtherIngredients(),
            usedPossibleIngredients = getUsedPossibleIngredients(),
            finalPrice = _viewModel.getFinalPrice()
        )
    }

    private fun getUnusedOtherIngredients(): ArrayList<IngredientItem> {
        val list = ArrayList<IngredientItem>()
        for (ingredientItem in _viewModel.observer.possibleIngredients) {
            if (_viewModel.item.value!!.dish.details.otherIngredients.containsKey(ingredientItem.id)) {
                list.add(ingredientItem)
            }
        }
        return list
    }

    private fun getUsedPossibleIngredients(): ArrayList<IngredientItem> {
        val list = ArrayList<IngredientItem>()
        for (ingredientItem in _viewModel.observer.otherIngredients) {
            if (_viewModel.item.value!!.dish.details.possibleIngredients.containsKey(ingredientItem.id)) {
                list.add(ingredientItem)
            }
        }
        return list
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