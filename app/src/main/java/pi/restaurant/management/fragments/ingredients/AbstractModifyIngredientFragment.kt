package pi.restaurant.management.fragments.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import pi.restaurant.management.R
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.Ingredient
import pi.restaurant.management.databinding.FragmentModifyIngredientBinding
import pi.restaurant.management.fragments.AbstractModifyItemFragment


abstract class AbstractModifyIngredientFragment : AbstractModifyItemFragment() {

    private var _binding: FragmentModifyIngredientBinding? = null
    val binding get() = _binding!!

    override val databasePath = "ingredients"
    override val linearLayout get() = binding.linearLayout
    override val saveButton get() = binding.buttonSave
    override val removeButton get() = binding.buttonRemove
    override var itemId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyIngredientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initializeUI() {
        binding.progress.progressBar.visibility = View.GONE
        initializeSpinner()
        setSaveButtonListener()
    }

    private fun initializeSpinner() {
        val spinner: Spinner = binding.spinnerUnit
        ArrayAdapter.createFromResource(
            context!!,
            R.array.units,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    override fun getDataObject(): AbstractDataObject {
        val name = binding.editTextName.text.toString()
        val amount = binding.editTextAmount.text.toString().toInt()

        return Ingredient(itemId, name, amount, binding.spinnerUnit.selectedItemPosition)
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextName] = R.string.name
        map[binding.editTextAmount] = R.string.amount
        return map
    }
}