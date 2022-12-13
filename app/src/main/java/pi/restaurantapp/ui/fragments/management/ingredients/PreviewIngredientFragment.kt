package pi.restaurantapp.ui.fragments.management.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentPreviewIngredientBinding
import pi.restaurantapp.databinding.ToolbarNavigationPreviewBinding
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.enums.IngredientModificationType
import pi.restaurantapp.ui.dialogs.IngredientChangesDialog
import pi.restaurantapp.ui.fragments.AbstractPreviewItemFragment
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.ingredients.PreviewIngredientViewModel

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for PreviewIngredientFragment.
 * @see pi.restaurantapp.viewmodels.fragments.management.ingredients.PreviewIngredientViewModel ViewModel layer
 * @see pi.restaurantapp.logic.fragments.management.ingredients.PreviewIngredientLogic Model layer
 */
class PreviewIngredientFragment : AbstractPreviewItemFragment() {
    override val toolbarNavigation: ToolbarNavigationPreviewBinding get() = binding.toolbarNavigation
    override val editActionId = R.id.actionPreviewIngredientToEditIngredient
    override val backActionId = R.id.actionPreviewIngredientToIngredients
    override val viewModel: AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel: PreviewIngredientViewModel by viewModels()

    private var _binding: FragmentPreviewIngredientBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewIngredientBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    fun onClickButtonDelivery(id: String, unit: Int) {
        IngredientChangesDialog(
            requireContext(),
            unit,
            getString(R.string.delivery),
            getString(R.string.delivery_description, binding.textViewAmount.text)
        ) { amount ->
            _viewModel.updateIngredientAmount(id, amount, IngredientModificationType.DELIVERY, setNewAmount = { newAmount ->
                binding.textViewAmount.text = StringFormatUtils.formatAmountWithUnit(requireContext(), newAmount.toString(), unit)
            }, addNewAmountChange = { newAmountChange ->
                _viewModel.amountChanges.value?.add(0, newAmountChange)
                binding.recyclerViewAmountChanges.adapter?.notifyItemInserted(0)
            })
        }
    }

    fun onClickButtonCorrection(id: String, unit: Int) {
        IngredientChangesDialog(
            requireContext(),
            unit,
            getString(R.string.correction),
            getString(R.string.correction_description, binding.textViewAmount.text)
        ) { amount ->
            _viewModel.updateIngredientAmount(id, amount, IngredientModificationType.CORRECTION, setNewAmount = { newAmount ->
                binding.textViewAmount.text = StringFormatUtils.formatAmountWithUnit(requireContext(), newAmount.toString(), unit)
            }, addNewAmountChange = { newAmountChange ->
                _viewModel.amountChanges.value?.add(0, newAmountChange)
                binding.recyclerViewAmountChanges.adapter?.notifyItemInserted(0)
            })
        }
    }
}