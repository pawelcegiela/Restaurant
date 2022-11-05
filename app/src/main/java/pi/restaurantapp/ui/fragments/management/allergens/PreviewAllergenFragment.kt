package pi.restaurantapp.ui.fragments.management.allergens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentPreviewAllergenBinding
import pi.restaurantapp.databinding.ToolbarNavigationPreviewBinding
import pi.restaurantapp.model.fragments.management.AbstractPreviewItemViewModel
import pi.restaurantapp.model.fragments.management.allergens.PreviewAllergenViewModel
import pi.restaurantapp.objects.data.allergen.Allergen
import pi.restaurantapp.ui.RecyclerManager
import pi.restaurantapp.ui.adapters.ContainingItemsRecyclerAdapter
import pi.restaurantapp.ui.fragments.AbstractPreviewItemFragment
import pi.restaurantapp.utils.UserInterfaceUtils

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