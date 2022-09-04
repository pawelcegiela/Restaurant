package pi.restaurant.management.fragments.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.Ingredient
import pi.restaurant.management.databinding.FragmentPreviewIngredientBinding
import pi.restaurant.management.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.utils.StringFormatUtils

class PreviewIngredientFragment : AbstractPreviewItemFragment() {
    override val databasePath = "ingredients"
    override val linearLayout get() = binding.linearLayout
    override val editButton get() = binding.buttonEdit
    override val editActionId = R.id.actionPreviewIngredientToEditIngredient

    private var _binding: FragmentPreviewIngredientBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewIngredientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun fillInData(dataSnapshot: DataSnapshot) {
        val item = dataSnapshot.getValue<Ingredient>() ?: return
        binding.textViewName.text = item.name
        binding.textViewAmountWithUnit.text =
            StringFormatUtils.formatAmountWithUnit(context!!, item.amount, item.unit)
        binding.progress.progressBar.visibility = View.GONE
    }
}