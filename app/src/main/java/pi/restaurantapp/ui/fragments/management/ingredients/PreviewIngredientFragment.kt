package pi.restaurantapp.ui.fragments.management.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentPreviewIngredientBinding
import pi.restaurantapp.databinding.ToolbarNavigationPreviewBinding
import pi.restaurantapp.model.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.model.fragments.management.ingredients.PreviewIngredientViewModel
import pi.restaurantapp.objects.data.ingredient.Ingredient
import pi.restaurantapp.objects.data.ingredient.IngredientAmountChange
import pi.restaurantapp.objects.data.ingredient.IngredientDetails
import pi.restaurantapp.objects.enums.IngredientModificationType
import pi.restaurantapp.objects.enums.IngredientStatus
import pi.restaurantapp.objects.enums.Unit
import pi.restaurantapp.ui.RecyclerManager
import pi.restaurantapp.ui.adapters.AmountChangesRecyclerAdapter
import pi.restaurantapp.ui.adapters.ContainingItemsRecyclerAdapter
import pi.restaurantapp.ui.adapters.DishIngredientsRecyclerAdapter
import pi.restaurantapp.ui.dialogs.IngredientChangesDialog
import pi.restaurantapp.ui.fragments.AbstractPreviewItemFragment
import pi.restaurantapp.utils.StringFormatUtils
import pi.restaurantapp.utils.UserInterfaceUtils
import java.lang.Integer.max

class PreviewIngredientFragment : AbstractPreviewItemFragment() {
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation: ToolbarNavigationPreviewBinding get() = binding.toolbarNavigation
    override val editActionId = R.id.actionPreviewIngredientToEditIngredient
    override val backActionId = R.id.actionPreviewIngredientToIngredients
    override val viewModel: AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel: PreviewIngredientViewModel by viewModels()
    private var amountChanges: MutableList<IngredientAmountChange> = ArrayList()

    private var _binding: FragmentPreviewIngredientBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewIngredientBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun fillInData() {
        val item = _viewModel.item.value ?: Ingredient()

        if (!item.basic.subDish) {
            binding.textViewAmount.text =
                StringFormatUtils.formatAmountWithUnit(requireContext(), item.basic.amount.toString(), item.basic.unit)
            setButtonListeners(item.id, item.basic.unit)
        }

        binding.textViewType.text = if (item.basic.subDish) getString(R.string.sub_dish) else getString(R.string.ingredient)

        if (item.basic.subDish && item.details.subIngredients != null) {
            binding.recyclerViewSubIngredients.adapter =
                DishIngredientsRecyclerAdapter(item.details.subIngredients!!, this, IngredientStatus.BASE)
            binding.recyclerViewSubIngredients.layoutManager = RecyclerManager(context)
            UserInterfaceUtils.setRecyclerSize(binding.recyclerViewSubIngredients, item.details.subIngredients!!.size, requireContext())
        } else {
            binding.cardSubIngredients.visibility = View.GONE
        }

        if (item.details.containingDishes.isEmpty() && item.details.containingSubDishes.isEmpty()) {
            viewModel.setReadyToUnlock()
        } else {
            setContainingDishes(item.details)
            setContainingSubDishes(item.details)
        }

        val firstIndex = max(amountChanges.size - 10, 0)
        val lastIndex = item.details.amountChanges.size
        this.amountChanges = item.details.amountChanges.map { it.value }.sortedByDescending { it.date }.subList(firstIndex, lastIndex).toMutableList()
        setAmountChangesRecycler(Unit.getString(item.basic.unit, requireContext()))
    }

    private fun setButtonListeners(id: String, unit: Int) {
        binding.buttonDelivery.setOnClickListener {
            IngredientChangesDialog(
                requireContext(),
                unit,
                getString(R.string.delivery),
                getString(R.string.delivery_description, binding.textViewAmount.text)
            ) { amount ->
                _viewModel.updateIngredientAmount(id, amount, IngredientModificationType.DELIVERY, setNewAmount = { newAmount ->
                    binding.textViewAmount.text = StringFormatUtils.formatAmountWithUnit(requireContext(), newAmount.toString(), unit)
                }, addNewAmountChange = { newAmountChange ->
                    this.amountChanges.add(newAmountChange)
                    binding.recyclerViewAmountChanges.adapter?.notifyItemInserted(amountChanges.size - 1)
                })
            }
        }

        binding.buttonCorrection.setOnClickListener {
            IngredientChangesDialog(
                requireContext(),
                unit,
                getString(R.string.correction),
                getString(R.string.correction_description, binding.textViewAmount.text)
            ) { amount ->
                _viewModel.updateIngredientAmount(id, amount, IngredientModificationType.CORRECTION, setNewAmount = { newAmount ->
                    binding.textViewAmount.text = StringFormatUtils.formatAmountWithUnit(requireContext(), newAmount.toString(), unit)
                }, addNewAmountChange = { newAmountChange ->
                    this.amountChanges.add(0, newAmountChange)
                    binding.recyclerViewAmountChanges.adapter?.notifyItemInserted(0)
                })
            }
        }
    }

    private fun setContainingDishes(details: IngredientDetails) {
        val containingDishesIds = details.containingDishes.map { it.key }
        if (containingDishesIds.isEmpty()) {
            binding.textViewContainingDishes.visibility = View.GONE
            binding.recyclerViewDishesContaining.visibility = View.GONE
        } else {
            _viewModel.getContainingDishes(containingDishesIds)

            _viewModel.liveContainingDishes.observe(viewLifecycleOwner) { containingDishes ->
                if (containingDishes.size == containingDishesIds.size) {
                    binding.recyclerViewDishesContaining.adapter =
                        ContainingItemsRecyclerAdapter(containingDishes, this)
                    binding.recyclerViewDishesContaining.layoutManager = RecyclerManager(context)
                    UserInterfaceUtils.setRecyclerSize(binding.recyclerViewDishesContaining, containingDishes.size, requireContext())

                    if (details.containingSubDishes.size == _viewModel.containingSubDishes.size) {
                        viewModel.setReadyToUnlock()
                    }
                }
            }
        }
    }

    private fun setContainingSubDishes(details: IngredientDetails) {
        val containingSubDishesIds = details.containingSubDishes.map { it.key }
        if (containingSubDishesIds.isEmpty()) {
            binding.textViewContainingSubDishes.visibility = View.GONE
            binding.recyclerViewSubDishesContaining.visibility = View.GONE
        } else {
            _viewModel.getContainingSubDishes(containingSubDishesIds)

            _viewModel.liveContainingSubDishes.observe(viewLifecycleOwner) { containingSubDishes ->
                if (containingSubDishes.size == containingSubDishesIds.size) {
                    binding.recyclerViewSubDishesContaining.adapter =
                        ContainingItemsRecyclerAdapter(containingSubDishes, this)
                    binding.recyclerViewSubDishesContaining.layoutManager = RecyclerManager(context)
                    UserInterfaceUtils.setRecyclerSize(
                        binding.recyclerViewSubDishesContaining,
                        containingSubDishes.size,
                        requireContext()
                    )

                    if (details.containingDishes.size == _viewModel.containingDishes.size) {
                        viewModel.setReadyToUnlock()
                    }
                }
            }
        }
    }

    private fun setAmountChangesRecycler(unit: String) {
        binding.recyclerViewAmountChanges.adapter = AmountChangesRecyclerAdapter(this.amountChanges, this, unit)
        binding.recyclerViewAmountChanges.layoutManager = RecyclerManager(context)
    }
}