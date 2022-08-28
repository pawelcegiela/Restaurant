package pi.restaurant.management.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import pi.restaurant.management.R
import pi.restaurant.management.data.Dish
import pi.restaurant.management.databinding.FragmentModifyDishBinding
import pi.restaurant.management.fragments.AbstractModifyItemFragment
import pi.restaurant.management.utils.Utils
import java.util.HashMap

abstract class AbstractModifyDishFragment : AbstractModifyItemFragment() {

    private var _binding: FragmentModifyDishBinding? = null
    val binding get() = _binding!!

    override val databasePath = "menu"
    override val linearLayout get() = binding.linearLayout
    override val saveButton get() = binding.buttonSave
    override val removeButton get() = binding.buttonRemove
    override var itemId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyDishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initializeUI() {
        setSaveButtonListener()
    }

    private fun setSaveButtonListener() {
        saveButton.setOnClickListener {
            if (!Utils.checkRequiredFields(getEditTextMap(), this)) {
                return@setOnClickListener
            }

            val name = binding.editTextName.text.toString()
            val price = binding.editTextPrice.text.toString().toDouble()

            setValue(Dish(itemId, name, price))
        }
    }

    private fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextName] = R.string.name
        map[binding.editTextPrice] = R.string.price
        return map
    }
}