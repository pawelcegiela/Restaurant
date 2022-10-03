package pi.restaurant.management.fragments.allergens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.adapters.ContainingItemsRecyclerAdapter
import pi.restaurant.management.data.*
import pi.restaurant.management.databinding.FragmentPreviewAllergenBinding
import pi.restaurant.management.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.utils.SnapshotsPair
import pi.restaurant.management.utils.SubItemUtils

class PreviewAllergenFragment : AbstractPreviewItemFragment() {
    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val cardSetNavigation get() = binding.cardSetNavigation
    override val editActionId = R.id.actionPreviewAllergenToEditAllergen
    override val backActionId = R.id.actionPreviewAllergenToAllergens
    override val viewModel : AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel : PreviewAllergenViewModel by viewModels()

    private var _binding: FragmentPreviewAllergenBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewAllergenBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getItem(snapshotsPair: SnapshotsPair) : Allergen {
        val basic = snapshotsPair.basic?.getValue<AllergenBasic>() ?: AllergenBasic()
        val details = snapshotsPair.details?.getValue<AllergenDetails>() ?: AllergenDetails()
        return Allergen(itemId, basic, details)
    }

    override fun fillInData(snapshotsPair: SnapshotsPair) {
        val item = getItem(snapshotsPair)
        binding.textViewName.text = item.basic.name
        if (item.details.description.isNotEmpty()) {
            binding.textViewDescription.text = item.details.description
        }

        if (item.details.containingDishes.isEmpty()) {
            binding.cardContainingDishes.visibility = View.GONE
            viewModel.liveReadyToUnlock.value = true
        } else {
            setContainingDishes(item.details.containingDishes.map { it.key })
        }
    }

    private fun setContainingDishes(containingDishesIds: List<String>) {
        _viewModel.getContainingDishes(containingDishesIds)

        _viewModel.liveContainingDishes.observe(viewLifecycleOwner) { containingDishes ->
            if (containingDishes.size == containingDishesIds.size) {
                binding.recyclerViewDishesContaining.adapter =
                    ContainingItemsRecyclerAdapter(containingDishes, this)
                SubItemUtils.setRecyclerSize(binding.recyclerViewDishesContaining, containingDishes.size, requireContext())
                viewModel.liveReadyToUnlock.value = true
            }
        }
    }
}