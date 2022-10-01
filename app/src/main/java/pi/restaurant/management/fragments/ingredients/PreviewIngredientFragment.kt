package pi.restaurant.management.fragments.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.adapters.DishIngredientsRecyclerAdapter
import pi.restaurant.management.data.*
import pi.restaurant.management.databinding.FragmentPreviewIngredientBinding
import pi.restaurant.management.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.utils.SnapshotsPair
import pi.restaurant.management.utils.StringFormatUtils
import pi.restaurant.management.utils.SubItemUtils

class PreviewIngredientFragment : AbstractPreviewItemFragment() {
    override val linearLayout get() = binding.linearLayout
    override val cardSetNavigation get() = binding.cardSetNavigation
    override val editActionId = R.id.actionPreviewIngredientToEditIngredient
    override val backActionId = R.id.actionPreviewIngredientToIngredients
    override val viewModel : AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel : PreviewIngredientViewModel by viewModels()

    private var _binding: FragmentPreviewIngredientBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewIngredientBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getItem(snapshotsPair: SnapshotsPair) : Ingredient {
        val basic = snapshotsPair.basic?.getValue<IngredientBasic>() ?: IngredientBasic()
        val details = snapshotsPair.details?.getValue<IngredientDetails>() ?: IngredientDetails()
        return Ingredient(itemId, basic, details)
    }

    override fun fillInData(snapshotsPair: SnapshotsPair) {
        val item = getItem(snapshotsPair)

        binding.textViewName.text = item.basic.name
        binding.textViewAmount.text =
            StringFormatUtils.formatAmountWithUnit(requireContext(), item.basic.amount, item.basic.unit)
        binding.checkBoxSubDish.isChecked = item.basic.subDish

        if (item.details.subIngredients != null) {
            binding.recyclerViewSubIngredients.adapter =
                DishIngredientsRecyclerAdapter(item.details.subIngredients!!, this, 0)
            SubItemUtils.setRecyclerSize(binding.recyclerViewSubIngredients, item.details.subIngredients!!.size, requireContext())
        } else {
            SubItemUtils.setRecyclerSize(binding.recyclerViewSubIngredients, 0, requireContext())
        }

        binding.progress.progressBar.visibility = View.GONE
    }
}