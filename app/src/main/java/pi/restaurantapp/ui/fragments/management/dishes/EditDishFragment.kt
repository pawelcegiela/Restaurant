package pi.restaurantapp.ui.fragments.management.dishes

import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.model.fragments.management.AbstractModifyItemViewModel
import pi.restaurantapp.model.fragments.management.dishes.EditDishViewModel
import pi.restaurantapp.objects.data.dish.Dish

class EditDishFragment : AbstractModifyDishFragment() {

    override val nextActionId = R.id.actionEditDishToDishes
    override val saveMessageId = R.string.dish_modified
    override val removeMessageId = R.string.dish_removed
    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : EditDishViewModel by viewModels()

    override fun initializeUI() {
        itemId = arguments?.getString("id").toString()

        initializeSpinners()
        initializeCheckBoxListener()
        getIngredientListAndSetIngredientButtons()
        getAllergenListAndSetAllergenButtons()
    }

    override fun fillInData() {
        val data = _viewModel.item.value ?: Dish()
        binding.editTextName.setText(data.basic.name)
        binding.editTextDescription.setText(data.details.description)
        binding.editTextRecipe.setText(data.details.recipe)
        binding.checkBoxActive.isChecked = data.basic.isActive
        binding.editTextBasePrice.setText(data.basic.basePrice)
        binding.checkBoxDiscount.isChecked = data.basic.isDiscounted
        binding.editTextDiscountPrice.isEnabled = data.basic.isDiscounted
        binding.editTextDiscountPrice.setText(data.basic.discountPrice)
        binding.spinnerDishType.setSelection(data.basic.dishType)
        binding.editTextAmount.setText(data.details.amount)
        binding.spinnerUnit.setSelection(data.details.unit)

        baseIngredientsList = data.details.baseIngredients.toList().map { it.second }.toMutableList()
        otherIngredientsList = data.details.otherIngredients.toList().map { it.second }.toMutableList()
        possibleIngredientsList = data.details.possibleIngredients.toList().map { it.second }.toMutableList()
        allergensList = data.details.allergens.toList().map { it.second }.toMutableList()
        setIngredientViews()
        setAllergenViews()

        setNavigationCardsSaveRemove()
        finishLoading()
    }
}