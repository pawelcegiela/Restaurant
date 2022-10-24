package pi.restaurantapp.ui.dialogs

import android.app.Dialog
import android.text.Editable
import android.view.View
import androidx.fragment.app.Fragment
import pi.restaurantapp.databinding.DialogIngredientPropertiesBinding
import pi.restaurantapp.objects.data.ingredient.IngredientItem
import pi.restaurantapp.objects.enums.IngredientStatus
import pi.restaurantapp.objects.enums.Unit
import pi.restaurantapp.ui.fragments.management.dishes.AbstractModifyDishFragment
import pi.restaurantapp.ui.fragments.management.ingredients.AbstractModifyIngredientFragment
import java.math.BigDecimal

class IngredientPropertiesDialog(
    val fragment: Fragment,
    private val item: Pair<IngredientItem, IngredientStatus>,
    private val isNew: Boolean
) :
    Dialog(fragment.requireContext()) {
    val binding: DialogIngredientPropertiesBinding = DialogIngredientPropertiesBinding.inflate(layoutInflater, null, false)

    init {
        setContentView(binding.root)
        setContent()
        setListener()
        show()
    }

    private fun setContent() {
        if (fragment is AbstractModifyIngredientFragment) {
            binding.radioGroupIngredientType.visibility = View.GONE
            binding.editTextExtraPrice.visibility = View.GONE
        }
        binding.textViewUnit.text = Unit.getString(item.first.unit, fragment.requireContext())
        when (item.second) {
            IngredientStatus.BASE -> binding.radioBaseIngredient.isChecked = true
            IngredientStatus.OTHER -> binding.radioOtherIngredient.isChecked = true
            IngredientStatus.POSSIBLE -> binding.radioPossibleIngredient.isChecked = true
        }

        if (BigDecimal(item.first.amount) != BigDecimal.ZERO) {
            binding.editTextAmount.setText(item.first.amount)
        }
        setExtraPriceField(binding.radioPossibleIngredient.isChecked)
    }

    private fun setListener() {
        binding.toolbarNavigation.cardRemove.setOnClickListener {
            removeItem()
            dismiss()
        }
        binding.toolbarNavigation.cardSave.setOnClickListener {
            saveItem(item, buildNewItem(item.first))
            dismiss()
        }
        binding.radioPossibleIngredient.setOnCheckedChangeListener { _, isChecked ->
            setExtraPriceField(isChecked)
        }
    }

    private fun setExtraPriceField(isChecked: Boolean) {
        if (isChecked) {
            binding.editTextExtraPrice.isEnabled = true
            if (BigDecimal(item.first.extraPrice) != BigDecimal.ZERO) {
                binding.editTextExtraPrice.setText(item.first.extraPrice)
            }
        } else {
            binding.editTextExtraPrice.isEnabled = false
            binding.editTextExtraPrice.setText("")
        }
    }

    private fun buildNewItem(oldItem: IngredientItem): Pair<IngredientItem, IngredientStatus> {
        val ingredientItem = IngredientItem(oldItem.id, oldItem.name, oldItem.unit)
        ingredientItem.amount = getProperValue(binding.editTextAmount.text)
        ingredientItem.extraPrice = getProperValue(binding.editTextExtraPrice.text)

        val status = if (binding.radioBaseIngredient.isChecked) {
            IngredientStatus.BASE
        } else if (binding.radioOtherIngredient.isChecked) {
            IngredientStatus.OTHER
        } else {
            IngredientStatus.POSSIBLE
        }

        return Pair(ingredientItem, status)
    }

    private fun getProperValue(text: Editable?) : String {
        return try {
            BigDecimal(text.toString())
            text.toString()
        } catch (ex: NumberFormatException) {
            "0.0"
        }
    }

    private fun removeItem() {
        if (fragment is AbstractModifyDishFragment) {
            fragment.removeIngredient(item)
        } else if (fragment is AbstractModifyIngredientFragment) {
            fragment.removeSubIngredient(item)
        }
    }

    private fun saveItem(oldItem: Pair<IngredientItem, IngredientStatus>, newItem: Pair<IngredientItem, IngredientStatus>) {
        if (fragment is AbstractModifyDishFragment) {
            fragment.saveIngredient(oldItem, newItem, isNew)
        } else if (fragment is AbstractModifyIngredientFragment) {
            fragment.saveSubIngredient(oldItem.first, newItem.first, isNew)
        }
    }
}