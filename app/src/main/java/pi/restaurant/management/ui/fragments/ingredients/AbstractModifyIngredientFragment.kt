package pi.restaurant.management.ui.fragments.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import pi.restaurant.management.R
import pi.restaurant.management.databinding.FragmentModifyIngredientBinding
import pi.restaurant.management.model.fragments.ingredients.AbstractModifyIngredientViewModel
import pi.restaurant.management.objects.data.SplitDataObject
import pi.restaurant.management.objects.data.ingredient.IngredientBasic
import pi.restaurant.management.objects.data.ingredient.IngredientDetails
import pi.restaurant.management.objects.data.ingredient.IngredientItem
import pi.restaurant.management.objects.enums.IngredientStatus
import pi.restaurant.management.objects.enums.Unit
import pi.restaurant.management.ui.adapters.DishIngredientsRecyclerAdapter
import pi.restaurant.management.ui.fragments.AbstractModifyItemFragment
import pi.restaurant.management.ui.listeners.AddIngredientButtonListener
import pi.restaurant.management.ui.views.DialogIngredientProperties
import pi.restaurant.management.ui.views.SpinnerAdapter
import pi.restaurant.management.utils.StringFormatUtils
import pi.restaurant.management.utils.UserInterfaceUtils


abstract class AbstractModifyIngredientFragment : AbstractModifyItemFragment() {

    private var _binding: FragmentModifyIngredientBinding? = null
    val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation get() = binding.toolbarNavigation
    override var itemId = ""

    var subIngredientsList: MutableList<IngredientItem> = ArrayList()
    lateinit var allIngredients: MutableList<IngredientBasic>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyIngredientBinding.inflate(inflater, container, false)
        linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun initializeUI() {
        initializeSpinner()
        setCheckBoxListener()
    }

    private fun initializeSpinner() {
        binding.spinnerUnit.adapter = SpinnerAdapter(requireContext(), Unit.getArrayOfStrings(requireContext()))
    }

    private fun setCheckBoxListener() {
        binding.checkBoxSubDish.setOnCheckedChangeListener { _, isChecked ->
            binding.cardSubIngredients.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
    }

    fun getIngredientListAndSetIngredientButton() {
        (viewModel as AbstractModifyIngredientViewModel).getAllIngredients()
        (viewModel as AbstractModifyIngredientViewModel).liveAllIngredients.observe(viewLifecycleOwner) { list ->
            allIngredients = list
            UserInterfaceUtils.setRecyclerSize(binding.recyclerViewSubIngredients, subIngredientsList.size, requireContext())
            binding.buttonAddSubIngredient.setOnClickListener(
                AddIngredientButtonListener(
                    subIngredientsList,
                    allIngredients,
                    this@AbstractModifyIngredientFragment
                )
            )
            binding.recyclerViewSubIngredients.adapter =
                DishIngredientsRecyclerAdapter(subIngredientsList, this@AbstractModifyIngredientFragment, IngredientStatus.BASE)
        }
    }

    fun changeSubIngredientProperties(ingredientItem: IngredientItem) {
        DialogIngredientProperties(this, Pair(ingredientItem, IngredientStatus.BASE), false)
    }

    fun removeSubIngredient(item: Pair<IngredientItem, IngredientStatus>) {
        val itemPosition = subIngredientsList.indexOf(item.first)
        subIngredientsList.remove(item.first)
        binding.recyclerViewSubIngredients.adapter?.notifyItemRemoved(itemPosition)
        UserInterfaceUtils.setRecyclerSize(binding.recyclerViewSubIngredients, subIngredientsList.size, requireContext())
    }

    fun saveSubIngredient(
        oldItem: IngredientItem,
        newItem: IngredientItem,
        isNew: Boolean
    ) {
        if (isNew) {
            subIngredientsList.add(newItem)
            binding.recyclerViewSubIngredients.adapter?.notifyItemInserted(subIngredientsList.indexOf(newItem))
        } else {
            val itemPosition = subIngredientsList.indexOf(oldItem)
            subIngredientsList
                .find { it == oldItem }.also { it?.amount = newItem.amount }
                .also { it?.extraPrice = newItem.extraPrice }
            binding.recyclerViewSubIngredients.adapter?.notifyItemChanged(itemPosition)
        }

        UserInterfaceUtils.setRecyclerSize(binding.recyclerViewSubIngredients, subIngredientsList.size, requireContext())
    }

    override fun getDataObject(): SplitDataObject {
        val ingredient = (viewModel as AbstractModifyIngredientViewModel).getPreviousItem()
        itemId = itemId.ifEmpty { StringFormatUtils.formatId() }

        val basic = IngredientBasic(
            id = itemId,
            name = binding.editTextName.text.toString(),
            amount = if (!binding.checkBoxSubDish.isChecked) binding.editTextAmount.text.toString().toInt() else 0,
            unit = binding.spinnerUnit.selectedItemPosition,
            subDish = binding.checkBoxSubDish.isChecked,
        )
        val details = IngredientDetails(
            id = itemId,
            subIngredients = if (binding.checkBoxSubDish.isChecked) subIngredientsList else null,
            containingDishes = ingredient.details.containingDishes,
            containingSubDishes = ingredient.details.containingSubDishes
        )

        return SplitDataObject(itemId, basic, details)
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextName] = R.string.name
        map[binding.editTextAmount] = R.string.amount
        return map
    }
}