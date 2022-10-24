package pi.restaurantapp.ui.fragments.management.allergens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentModifyAllergenBinding
import pi.restaurantapp.model.fragments.management.allergens.AbstractModifyAllergenViewModel
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.objects.data.allergen.AllergenDetails
import pi.restaurantapp.ui.fragments.management.AbstractModifyItemFragment
import pi.restaurantapp.utils.StringFormatUtils

abstract class AbstractModifyAllergenFragment : AbstractModifyItemFragment() {

    private var _binding: FragmentModifyAllergenBinding? = null
    val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation get() = binding.toolbarNavigation
    override var itemId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyAllergenBinding.inflate(inflater, container, false)
        linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun getDataObject(): SplitDataObject {
        val allergen = (viewModel as AbstractModifyAllergenViewModel).getPreviousItem()
        itemId = itemId.ifEmpty { StringFormatUtils.formatId() }
        val basic = AllergenBasic(
            id = itemId,
            name = binding.editTextName.text.toString()
        )
        val details = AllergenDetails(
            id = itemId,
            description = binding.editTextDescription.text.toString(),
            containingDishes = allergen.details.containingDishes
        )

        return SplitDataObject(itemId, basic, details)
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextName] = R.string.name
        return map
    }
}