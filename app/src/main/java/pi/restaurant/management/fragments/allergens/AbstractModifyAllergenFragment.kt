package pi.restaurant.management.fragments.allergens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import pi.restaurant.management.R
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.Allergen
import pi.restaurant.management.databinding.FragmentModifyAllergenBinding
import pi.restaurant.management.fragments.AbstractModifyItemFragment
import java.util.HashMap

abstract class AbstractModifyAllergenFragment : AbstractModifyItemFragment() {

    private var _binding: FragmentModifyAllergenBinding? = null
    val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val saveButton get() = binding.buttonSave
    override val removeButton get() = binding.buttonRemove
    override var itemId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyAllergenBinding.inflate(inflater, container, false)
        linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun initializeUI() {
        finishLoading()
        setSaveButtonListener()
    }

    override fun getDataObject(): AbstractDataObject {
        val name = binding.editTextName.text.toString()

        return Allergen(itemId, name)
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextName] = R.string.name
        return map
    }
}