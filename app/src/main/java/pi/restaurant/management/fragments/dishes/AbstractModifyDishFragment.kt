package pi.restaurant.management.fragments.dishes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import pi.restaurant.management.R
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.Dish
import pi.restaurant.management.databinding.FragmentModifyDishBinding
import pi.restaurant.management.fragments.AbstractModifyItemFragment
import java.util.HashMap

abstract class AbstractModifyDishFragment : AbstractModifyItemFragment() {

    private var _binding: FragmentModifyDishBinding? = null
    val binding get() = _binding!!

    override val databasePath = "dishes"
    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val saveButton get() = binding.buttonSave
    override val removeButton get() = binding.buttonRemove
    override var itemId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyDishBinding.inflate(inflater, container, false)
        linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun initializeUI() {
        finishLoading()
        initializeSpinners()
        setSaveButtonListener()
    }

    override fun getDataObject(): AbstractDataObject {
        return Dish(
            id = itemId,
            name = binding.editTextName.text.toString(),
            description = binding.editTextDescription.text.toString(),
            isActive = binding.checkBoxActive.isChecked,
            basePrice = binding.editTextBasePrice.text.toString().toDouble(),
            isDiscounted = binding.checkBoxDiscount.isChecked,
            discountPrice = binding.editTextDiscountPrice.text.toString().toDouble(),
            dishType = binding.spinnerDishType.selectedItemId.toInt(),
            amount = binding.editTextAmount.text.toString().toDouble(),
            unit = binding.spinnerUnit.selectedItemId.toInt()
        )
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

    private fun initializeSpinners() {
        val spinnerUnit: Spinner = binding.spinnerUnit
        ArrayAdapter.createFromResource(
            context!!,
            R.array.units,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerUnit.adapter = adapter
        }

        val spinnerDishType: Spinner = binding.spinnerDishType
        ArrayAdapter.createFromResource(
            context!!,
            R.array.dish_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDishType.adapter = adapter
        }
    }
}