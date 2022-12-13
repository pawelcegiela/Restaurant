package pi.restaurantapp.ui.fragments.management.dishes

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentPreviewDishBinding
import pi.restaurantapp.databinding.ToolbarNavigationPreviewBinding
import pi.restaurantapp.ui.fragments.AbstractPreviewItemFragment
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.dishes.PreviewDishViewModel

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for PreviewDishFragment.
 * @see pi.restaurantapp.viewmodels.fragments.management.dishes.PreviewDishViewModel ViewModel layer
 * @see pi.restaurantapp.logic.fragments.management.dishes.PreviewDishLogic Model layer
 */
class PreviewDishFragment : AbstractPreviewItemFragment() {
    override val toolbarNavigation: ToolbarNavigationPreviewBinding get() = binding.toolbarNavigation
    override val editActionId = R.id.actionPreviewDishToEditDish
    override val backActionId = R.id.actionPreviewDishToDishes
    override val viewModel: AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel: PreviewDishViewModel by viewModels()

    private var _binding: FragmentPreviewDishBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewDishBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun initializeExtraData() {
        if (_viewModel.item.value?.basic?.isDiscounted == true) {
            binding.textViewOriginalPrice.paintFlags = binding.textViewOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        viewModel.setReadyToUnlock()
    }

    fun onClickDetails(showMore: Boolean) {
        binding.linearLayoutDetailsUnexpanded.visibility = if (showMore) View.GONE else View.VISIBLE
        binding.linearLayoutDetailsExpanded.visibility = if (showMore) View.VISIBLE else View.GONE
    }

    fun onClickIngredients(showMore: Boolean) {
        binding.linearLayoutIngredientsUnexpanded.visibility = if (showMore) View.GONE else View.VISIBLE
        binding.linearLayoutIngredientsExpanded.visibility = if (showMore) View.VISIBLE else View.GONE
    }
}