package pi.restaurantapp.ui.fragments.management.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentModifyIngredientBinding
import pi.restaurantapp.objects.data.ingredient.IngredientItem
import pi.restaurantapp.objects.enums.IngredientStatus
import pi.restaurantapp.objects.enums.Unit
import pi.restaurantapp.ui.dialogs.IngredientPropertiesDialog
import pi.restaurantapp.ui.fragments.AbstractModifyItemFragment
import pi.restaurantapp.viewmodels.fragments.management.ingredients.AbstractModifyIngredientViewModel


abstract class AbstractModifyIngredientFragment : AbstractModifyItemFragment() {

    private var _binding: FragmentModifyIngredientBinding? = null
    val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation get() = binding.toolbarNavigation
    override var itemId = ""

    private val _viewModel get() = viewModel as AbstractModifyIngredientViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyIngredientBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.fragment = this
        binding.lifecycleOwner = this
        binding.observer = _viewModel.observer
        linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    fun onCheckedChangedSubDish(isChecked: Boolean) {
        binding.cardSubIngredients.visibility = if (isChecked) View.VISIBLE else View.GONE
        binding.spinnerUnit.isEnabled = !isChecked
        if (isChecked) {
            binding.spinnerUnit.setSelection(Unit.PIECE.ordinal)
        }
    }

    fun changeSubIngredientProperties(ingredientItem: IngredientItem) {
        IngredientPropertiesDialog(this, Pair(ingredientItem, IngredientStatus.BASE), false)
    }

    fun removeSubIngredient(item: Pair<IngredientItem, IngredientStatus>) {
        val itemPosition = _viewModel.observer.subIngredients.indexOf(item.first)
        _viewModel.observer.subIngredients.remove(item.first)
        binding.recyclerViewSubIngredients.adapter?.notifyItemRemoved(itemPosition)
    }

    fun saveSubIngredient(
        oldItem: IngredientItem,
        newItem: IngredientItem,
        isNew: Boolean
    ) {
        if (isNew) {
            _viewModel.observer.subIngredients.add(newItem)
            binding.recyclerViewSubIngredients.adapter?.notifyItemInserted(_viewModel.observer.subIngredients.indexOf(newItem))
        } else {
            val itemPosition = _viewModel.observer.subIngredients.indexOf(oldItem)
            _viewModel.observer.subIngredients
                .find { it == oldItem }.also { it?.amount = newItem.amount }
                .also { it?.extraPrice = newItem.extraPrice }
            binding.recyclerViewSubIngredients.adapter?.notifyItemChanged(itemPosition)
        }
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextName] = R.string.name
        return map
    }
}