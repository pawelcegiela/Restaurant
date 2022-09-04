package pi.restaurant.management.fragments.dishes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
        setSaveButtonListener()
    }

    override fun getDataObject(): AbstractDataObject {
        val name = binding.editTextName.text.toString()
        val price = binding.editTextPrice.text.toString().toDouble()

        return Dish(itemId, name, price)
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextName] = R.string.name
        map[binding.editTextPrice] = R.string.price
        return map
    }
}