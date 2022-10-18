package pi.restaurant.management.ui.fragments.allergens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.databinding.FragmentPreviewAllergenBinding
import pi.restaurant.management.databinding.ToolbarNavigationPreviewBinding
import pi.restaurant.management.model.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.model.fragments.allergens.PreviewAllergenViewModel
import pi.restaurant.management.objects.data.allergen.Allergen
import pi.restaurant.management.ui.RecyclerManager
import pi.restaurant.management.ui.adapters.ContainingItemsRecyclerAdapter
import pi.restaurant.management.ui.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.utils.UserInterfaceUtils

class PreviewAllergenFragment : AbstractPreviewItemFragment() {
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation: ToolbarNavigationPreviewBinding get() = binding.toolbarNavigation
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
        binding.vm = _viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun fillInData() {
        val item = _viewModel.item.value ?: Allergen()

        if (item.details.containingDishes.isEmpty()) {
            binding.cardContainingDishes.visibility = View.GONE
            viewModel.setReadyToUnlock()
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
                binding.recyclerViewDishesContaining.layoutManager = RecyclerManager(context)
                UserInterfaceUtils.setRecyclerSize(binding.recyclerViewDishesContaining, containingDishes.size, requireContext())
                viewModel.setReadyToUnlock()
            }
        }
    }
}