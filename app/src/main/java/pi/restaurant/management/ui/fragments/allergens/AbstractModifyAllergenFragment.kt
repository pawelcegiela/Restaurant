package pi.restaurant.management.ui.fragments.allergens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import pi.restaurant.management.R
import pi.restaurant.management.databinding.FragmentModifyAllergenBinding
import pi.restaurant.management.model.fragments.allergens.AbstractModifyAllergenViewModel
import pi.restaurant.management.objects.data.SplitDataObject
import pi.restaurant.management.objects.data.allergen.AllergenBasic
import pi.restaurant.management.objects.data.allergen.AllergenDetails
import pi.restaurant.management.ui.fragments.AbstractModifyItemFragment
import pi.restaurant.management.utils.StringFormatUtils

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