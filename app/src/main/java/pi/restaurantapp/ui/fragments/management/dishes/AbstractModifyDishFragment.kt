package pi.restaurantapp.ui.fragments.management.dishes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentModifyDishBinding
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.objects.data.ingredient.IngredientItem
import pi.restaurantapp.objects.enums.IngredientStatus
import pi.restaurantapp.ui.dialogs.AddAllergenDialog
import pi.restaurantapp.ui.dialogs.IngredientPropertiesDialog
import pi.restaurantapp.ui.fragments.AbstractModifyItemFragment
import pi.restaurantapp.viewmodels.fragments.management.dishes.AbstractModifyDishViewModel

/**
 * Abstract class responsible for direct communication and displaying information to the user (View layer) for AbstractModifyDishFragment.
 * @see pi.restaurantapp.viewmodels.fragments.management.dishes.AbstractModifyDishViewModel ViewModel layer
 * @see pi.restaurantapp.logic.fragments.management.dishes.AbstractModifyDishLogic Model layer
 */
abstract class AbstractModifyDishFragment : AbstractModifyItemFragment() {

    private var _binding: FragmentModifyDishBinding? = null
    val binding get() = _binding!!

    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation get() = binding.toolbarNavigation
    override var itemId = ""

    private val _viewModel get() = viewModel as AbstractModifyDishViewModel

    private val lists
        get() = arrayListOf(
            _viewModel.observer.baseIngredients,
            _viewModel.observer.otherIngredients,
            _viewModel.observer.possibleIngredients
        )
    private val recyclers
        get() = arrayListOf(
            binding.recyclerViewBaseIngredients,
            binding.recyclerViewOtherIngredients,
            binding.recyclerViewPossibleIngredients
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyDishBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextName] = R.string.name
        map[binding.editTextBasePrice] = R.string.base_price
        if (binding.checkBoxDiscount.isChecked) {
            map[binding.editTextDiscountPrice] = R.string.discount_price
        }
        map[binding.editTextAmount] = R.string.amount
        return map
    }

    fun onCheckBoxChanged(isChecked: Boolean) {
        binding.editTextDiscountPrice.isEnabled = isChecked
        if (isChecked) {
            binding.editTextDiscountPrice.setText("")
        }
    }

    fun onClickAddAllergen() {
        AddAllergenDialog(
            requireContext(),
            _viewModel.observer.allergens,
            _viewModel.allAllergens.value!!,
            getString(R.string.select_allergen)
        ) { newAllergen ->
            _viewModel.observer.allergens.add(newAllergen)
            binding.recyclerViewAllergens.adapter?.notifyItemInserted(_viewModel.observer.allergens.indexOf(newAllergen))
        }
    }

    fun changeIngredientProperties(ingredientItem: IngredientItem, originalList: IngredientStatus) {
        IngredientPropertiesDialog(this, Pair(ingredientItem, originalList), false)
    }

    fun removeIngredient(item: Pair<IngredientItem, IngredientStatus>) {
        val itemPosition = lists[item.second.ordinal].indexOf(item.first)
        lists[item.second.ordinal].remove(item.first)
        recyclers[item.second.ordinal].adapter?.notifyItemRemoved(itemPosition)
    }

    fun saveIngredient(
        oldItem: Pair<IngredientItem, IngredientStatus>,
        newItem: Pair<IngredientItem, IngredientStatus>,
        isNew: Boolean
    ) {
        if (isNew) {
            lists[newItem.second.ordinal].add(newItem.first)
        } else if (oldItem.second == newItem.second) {
            lists[newItem.second.ordinal]
                .find { it == oldItem.first }.also { it?.amount = newItem.first.amount }
                .also { it?.extraPrice = newItem.first.extraPrice }
        } else {
            val oldListPosition = lists[oldItem.second.ordinal].indexOf(oldItem.first)
            lists[oldItem.second.ordinal].remove(oldItem.first)
            lists[newItem.second.ordinal].add(newItem.first)

            recyclers[oldItem.second.ordinal].adapter?.notifyItemRemoved(oldListPosition)
        }
        recyclers[newItem.second.ordinal].adapter?.notifyItemInserted(lists[newItem.second.ordinal].indexOf(newItem.first))
    }

    fun removeAllergenItem(allergenItem: AllergenBasic) {
        val itemPosition = _viewModel.observer.allergens.indexOf(allergenItem)
        _viewModel.observer.allergens.remove(allergenItem)
        binding.recyclerViewAllergens.adapter?.notifyItemRemoved(itemPosition)
    }
}