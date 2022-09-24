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
import pi.restaurant.management.data.Ingredient
import pi.restaurant.management.databinding.FragmentPreviewIngredientBinding
import pi.restaurant.management.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.utils.StringFormatUtils
import pi.restaurant.management.utils.SubItemUtils

class PreviewIngredientFragment : AbstractPreviewItemFragment() {
    override val linearLayout get() = binding.linearLayout
    override val editButton get() = binding.buttonEdit
    override val editActionId = R.id.actionPreviewIngredientToEditIngredient
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

    override fun fillInData(dataSnapshot: DataSnapshot) {
        val item = dataSnapshot.getValue<Ingredient>() ?: return
        binding.textViewName.text = item.name
        binding.textViewAmountWithUnit.text =
            StringFormatUtils.formatAmountWithUnit(requireContext(), item.amount, item.unit)
        binding.checkBoxSubDish.isChecked = item.subDish

        if (item.subIngredients != null) {
            binding.recyclerViewSubIngredients.adapter =
                DishIngredientsRecyclerAdapter(item.subIngredients!!, this, 0)
            SubItemUtils.setRecyclerSize(binding.recyclerViewSubIngredients, item.subIngredients!!.size, requireContext())
        } else {
            SubItemUtils.setRecyclerSize(binding.recyclerViewSubIngredients, 0, requireContext())
        }

        binding.progress.progressBar.visibility = View.GONE
    }
}