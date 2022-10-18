package pi.restaurant.management.ui.fragments.dishes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import pi.restaurant.management.R
import pi.restaurant.management.databinding.FragmentModifyDishBinding
import pi.restaurant.management.model.fragments.dishes.AbstractModifyDishViewModel
import pi.restaurant.management.objects.data.SplitDataObject
import pi.restaurant.management.objects.data.allergen.AllergenBasic
import pi.restaurant.management.objects.data.dish.DishBasic
import pi.restaurant.management.objects.data.dish.DishDetails
import pi.restaurant.management.objects.data.ingredient.IngredientBasic
import pi.restaurant.management.objects.data.ingredient.IngredientItem
import pi.restaurant.management.objects.enums.DishType
import pi.restaurant.management.objects.enums.IngredientStatus
import pi.restaurant.management.objects.enums.Unit
import pi.restaurant.management.ui.RecyclerManager
import pi.restaurant.management.ui.adapters.DishAllergensRecyclerAdapter
import pi.restaurant.management.ui.adapters.DishIngredientsRecyclerAdapter
import pi.restaurant.management.ui.fragments.AbstractModifyItemFragment
import pi.restaurant.management.ui.listeners.AddAllergenButtonListener
import pi.restaurant.management.ui.listeners.AddIngredientButtonListener
import pi.restaurant.management.ui.dialogs.IngredientPropertiesDialog
import pi.restaurant.management.ui.adapters.SpinnerAdapter
import pi.restaurant.management.utils.StringFormatUtils
import pi.restaurant.management.utils.UserInterfaceUtils


abstract class AbstractModifyDishFragment : AbstractModifyItemFragment() {

    // TODO Change Recycler to ListView!

    private var _binding: FragmentModifyDishBinding? = null
    val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation get() = binding.toolbarNavigation
    override var itemId = ""

    var baseIngredientsList: MutableList<IngredientItem> = ArrayList()
    var otherIngredientsList: MutableList<IngredientItem> = ArrayList()
    var possibleIngredientsList: MutableList<IngredientItem> = ArrayList()
    var allergensList: MutableList<AllergenBasic> = ArrayList()
    private lateinit var allIngredients: MutableList<IngredientBasic>
    private lateinit var allAllergens: MutableList<AllergenBasic>

    private val lists get() = arrayListOf(baseIngredientsList, otherIngredientsList, possibleIngredientsList)
    private val recyclers
        get() = arrayListOf(
            binding.recyclerViewBaseIngredients,
            binding.recyclerViewOtherIngredients,
            binding.recyclerViewPossibleIngredients
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyDishBinding.inflate(inflater, container, false)
        linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun initializeUI() {
        finishLoading()
        initializeSpinners()
        initializeCheckBoxListener()
        setNavigationCardsSave()
        getIngredientListAndSetIngredientButtons()
        getAllergenListAndSetAllergenButtons()
    }

    override fun getDataObject(): SplitDataObject {
        val dish = (viewModel as AbstractModifyDishViewModel).getPreviousItem()
        itemId = itemId.ifEmpty { StringFormatUtils.formatId() }
        val discountPrice = if (binding.checkBoxDiscount.isChecked) {
            binding.editTextDiscountPrice.text.toString()
        } else {
            "0.0"
        }

        val basic = DishBasic(
            id = itemId,
            name = binding.editTextName.text.toString(),
            isActive = binding.checkBoxActive.isChecked,
            basePrice = binding.editTextBasePrice.text.toString(),
            isDiscounted = binding.checkBoxDiscount.isChecked,
            discountPrice = discountPrice,
            dishType = binding.spinnerDishType.selectedItemId.toInt()
        )
        val details = DishDetails(
            id = itemId,
            description = binding.editTextDescription.text.toString(),
            recipe = binding.editTextRecipe.text.toString(),
            baseIngredients = HashMap(baseIngredientsList.associateBy { it.id }),
            otherIngredients = HashMap(otherIngredientsList.associateBy { it.id }),
            possibleIngredients = HashMap(possibleIngredientsList.associateBy { it.id }),
            allergens = HashMap(allergensList.associateBy { it.id }),
            amount = binding.editTextAmount.text.toString(),
            unit = binding.spinnerUnit.selectedItemId.toInt(),
            containingOrders = dish.details.containingOrders
        )

        return SplitDataObject(itemId, basic, details)
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextName] = R.string.name
        map[binding.editTextBasePrice] = R.string.base_price
        if (binding.checkBoxDiscount.isChecked) {
            map[binding.editTextDiscountPrice] = R.string.discount_price
        }
        map[binding.editTextAmount] = R.string.amount
        return map
    }

    fun initializeSpinners() {
        binding.spinnerUnit.adapter = SpinnerAdapter(requireContext(), Unit.getArrayOfStrings(requireContext()))
        binding.spinnerDishType.adapter = SpinnerAdapter(requireContext(), DishType.getArrayOfStrings(requireContext()))
    }

    fun initializeCheckBoxListener() {
        binding.checkBoxDiscount.setOnCheckedChangeListener { _, isChecked ->
            binding.editTextDiscountPrice.isEnabled = isChecked
            if (isChecked) {
                binding.editTextDiscountPrice.setText("")
            }
        }
    }

    fun getIngredientListAndSetIngredientButtons() {
        (viewModel as AbstractModifyDishViewModel).getAllIngredients()
        (viewModel as AbstractModifyDishViewModel).liveAllIngredients.observe(viewLifecycleOwner) { list ->
            allIngredients = list
            setIngredientViews()
        }
    }

    fun setIngredientViews() {
        val list =
            ArrayList(baseIngredientsList).also { it.addAll(otherIngredientsList) }.also { it.addAll(possibleIngredientsList) }

        binding.buttonAddIngredient.setOnClickListener(
            AddIngredientButtonListener(
                list,
                allIngredients,
                this
            )
        )

        binding.recyclerViewBaseIngredients.adapter =
            DishIngredientsRecyclerAdapter(baseIngredientsList, this, IngredientStatus.BASE)
        UserInterfaceUtils.setRecyclerSize(binding.recyclerViewBaseIngredients, baseIngredientsList.size, requireContext())
        binding.recyclerViewBaseIngredients.layoutManager = RecyclerManager(context)

        binding.recyclerViewOtherIngredients.adapter =
            DishIngredientsRecyclerAdapter(otherIngredientsList, this, IngredientStatus.OTHER)
        UserInterfaceUtils.setRecyclerSize(binding.recyclerViewOtherIngredients, otherIngredientsList.size, requireContext())
        binding.recyclerViewOtherIngredients.layoutManager = RecyclerManager(context)

        binding.recyclerViewPossibleIngredients.adapter =
            DishIngredientsRecyclerAdapter(possibleIngredientsList, this, IngredientStatus.POSSIBLE)
        UserInterfaceUtils.setRecyclerSize(binding.recyclerViewPossibleIngredients, possibleIngredientsList.size, requireContext())
        binding.recyclerViewPossibleIngredients.layoutManager = RecyclerManager(context)
    }

    fun getAllergenListAndSetAllergenButtons() {
        (viewModel as AbstractModifyDishViewModel).getAllAllergens()
        (viewModel as AbstractModifyDishViewModel).liveAllAllergens.observe(viewLifecycleOwner) { list ->
            allAllergens = list
            setAllergenViews()
        }
    }

    fun setAllergenViews() {
        binding.buttonAddAllergen.setOnClickListener(
            AddAllergenButtonListener(
                requireContext(),
                allAllergens.map { it.name }.toMutableList(),
                allergensList,
                allAllergens,
                this
            )
        )

        binding.recyclerViewAllergens.adapter =
            DishAllergensRecyclerAdapter(allergensList, this)
        UserInterfaceUtils.setRecyclerSize(binding.recyclerViewAllergens, allergensList.size, requireContext())
        binding.recyclerViewAllergens.layoutManager = RecyclerManager(context)
    }

    fun changeIngredientProperties(ingredientItem: IngredientItem, originalList: IngredientStatus) {
        IngredientPropertiesDialog(this, Pair(ingredientItem, originalList), false)
    }

    fun removeIngredient(item: Pair<IngredientItem, IngredientStatus>) {
        val itemPosition = lists[item.second.ordinal].indexOf(item.first)
        lists[item.second.ordinal].remove(item.first)
        recyclers[item.second.ordinal].adapter?.notifyItemRemoved(itemPosition)
        UserInterfaceUtils.setRecyclerSize(recyclers[item.second.ordinal], lists[item.second.ordinal].size, requireContext())
    }

    fun saveIngredient(
        oldItem: Pair<IngredientItem, IngredientStatus>,
        newItem: Pair<IngredientItem, IngredientStatus>,
        isNew: Boolean
    ) {
        if (isNew) {
            lists[newItem.second.ordinal].add(newItem.first)
        } else if (oldItem.second == newItem.second) {
            lists[newItem.second.ordinal]
                .find { it == oldItem.first }.also { it?.amount = newItem.first.amount }
                .also { it?.extraPrice = newItem.first.extraPrice }
        } else {
            val oldListPosition = lists[oldItem.second.ordinal].indexOf(oldItem.first)
            lists[oldItem.second.ordinal].remove(oldItem.first)
            lists[newItem.second.ordinal].add(newItem.first)

            recyclers[oldItem.second.ordinal].adapter?.notifyItemRemoved(oldListPosition)
            UserInterfaceUtils.setRecyclerSize(recyclers[oldItem.second.ordinal], lists[oldItem.second.ordinal].size, requireContext())
        }
        recyclers[newItem.second.ordinal].adapter?.notifyItemInserted(lists[newItem.second.ordinal].indexOf(newItem.first))
        UserInterfaceUtils.setRecyclerSize(recyclers[newItem.second.ordinal], lists[newItem.second.ordinal].size, requireContext())
    }

    fun removeAllergenItem(allergenItem: AllergenBasic) {
        val itemPosition = allergensList.indexOf(allergenItem)
        allergensList.remove(allergenItem)
        binding.recyclerViewAllergens.adapter?.notifyItemRemoved(itemPosition)
        UserInterfaceUtils.setRecyclerSize(binding.recyclerViewAllergens, allergensList.size, requireContext())
    }

    fun addAllergenItem(allergenItem: AllergenBasic) {
        allergensList.add(allergenItem)
        binding.recyclerViewAllergens.adapter?.notifyItemInserted(allergensList.indexOf(allergenItem))
        UserInterfaceUtils.setRecyclerSize(binding.recyclerViewAllergens, allergensList.size, requireContext())
    }
}