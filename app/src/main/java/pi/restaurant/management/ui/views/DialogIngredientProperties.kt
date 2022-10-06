package pi.restaurant.management.ui.views

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.view.View
import androidx.fragment.app.Fragment
import pi.restaurant.management.objects.data.ingredient.IngredientItem
import pi.restaurant.management.databinding.DialogIngredientPropertiesBinding
import pi.restaurant.management.objects.enums.IngredientStatus
import pi.restaurant.management.ui.fragments.dishes.AbstractModifyDishFragment
import pi.restaurant.management.ui.fragments.ingredients.AbstractModifyIngredientFragment
import java.lang.NumberFormatException

class DialogIngredientProperties(
    val fragment: Fragment,
    private val item: Pair<IngredientItem, IngredientStatus>,
    private val isNew: Boolean
) :
    Dialog(fragment.requireContext()) {
    val binding: DialogIngredientPropertiesBinding = DialogIngredientPropertiesBinding.inflate(layoutInflater, null, false)

    init {
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContent()
        setListener()
        show()
    }

    private fun setContent() {
        if (fragment is AbstractModifyIngredientFragment) {
            binding.radioGroupIngredientType.visibility = View.GONE
            binding.editTextExtraPrice.visibility = View.GONE
        }
        when (item.second) {
            IngredientStatus.BASE -> binding.radioBaseIngredient.isChecked = true
            IngredientStatus.OTHER -> binding.radioOtherIngredient.isChecked = true
            IngredientStatus.POSSIBLE -> binding.radioPossibleIngredient.isChecked = true
        }

        if (item.first.amount != 0.0) {
            binding.editTextAmount.setText(item.first.amount.toString())
        }
        setExtraPriceField(binding.radioPossibleIngredient.isChecked)

    }

    private fun setListener() {
        binding.cardSetNavigation.cardBack.setOnClickListener {
            dismiss()
        }
        binding.cardSetNavigation.cardRemove.setOnClickListener {
            removeItem()
            dismiss()
        }
        binding.cardSetNavigation.cardSave.setOnClickListener {
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
            if (item.first.extraPrice != 0.0) {
                binding.editTextExtraPrice.setText(item.first.extraPrice.toString())
            }
        } else {
            binding.editTextExtraPrice.isEnabled = false
            binding.editTextExtraPrice.setText("")
        }
    }

    private fun buildNewItem(oldItem: IngredientItem): Pair<IngredientItem, IngredientStatus> {
        val ingredientItem = IngredientItem(oldItem.id, oldItem.name, oldItem.unit)
        ingredientItem.amount = getDoubleValue(binding.editTextAmount.text)
        ingredientItem.extraPrice = getDoubleValue(binding.editTextExtraPrice.text)

        val status = if (binding.radioBaseIngredient.isChecked) {
            IngredientStatus.BASE
        } else if (binding.radioOtherIngredient.isChecked) {
            IngredientStatus.OTHER
        } else {
            IngredientStatus.POSSIBLE
        }

        return Pair(ingredientItem, status)
    }

    private fun getDoubleValue(text: Editable?) : Double {
        return try {
            text.toString().toDouble()
        } catch (ex: NumberFormatException) {
            0.0
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