package pi.restaurant.management.fragments.dishes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.adapters.DishAllergensRecyclerAdapter
import pi.restaurant.management.adapters.DishIngredientsRecyclerAdapter
import pi.restaurant.management.data.*
import pi.restaurant.management.databinding.FragmentModifyDishBinding
import pi.restaurant.management.enums.DishType
import pi.restaurant.management.enums.IngredientItemState
import pi.restaurant.management.enums.Unit
import pi.restaurant.management.fragments.AbstractModifyItemFragment
import pi.restaurant.management.listeners.AllergenModifyDishOnClickListener
import pi.restaurant.management.listeners.IngredientModifyDishOnClickListener
import pi.restaurant.management.utils.StringFormatUtils
import pi.restaurant.management.utils.SubItemUtils


abstract class AbstractModifyDishFragment : AbstractModifyItemFragment() {

    // TODO Change Recycler to ListView!

    private var _binding: FragmentModifyDishBinding? = null
    val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val cardSetNavigation get() = binding.cardSetNavigation
    override var itemId = ""

    var baseIngredientsList: MutableList<IngredientItem> = ArrayList()
    var otherIngredientsList: MutableList<IngredientItem> = ArrayList()
    var possibleIngredientsList: MutableList<IngredientItem> = ArrayList()
    var allergensList: MutableList<AllergenBasic> = ArrayList()
    lateinit var allIngredients: MutableList<IngredientBasic>
    lateinit var allAllergens: MutableList<AllergenBasic>

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
        setNavigationCardsSave()
        getIngredientListAndSetIngredientButtons()
        getAllergenListAndSetAllergenButtons()
    }

    override fun getDataObject(): SplitDataObject {
        itemId = itemId.ifEmpty { StringFormatUtils.formatId() }
        val discountPrice = if (binding.checkBoxDiscount.isChecked) {
            binding.editTextDiscountPrice.text.toString().toDouble()
        } else {
            0.0
        }

        val basic = DishBasic(
            id = itemId,
            name = binding.editTextName.text.toString(),
            isActive = binding.checkBoxActive.isChecked,
            basePrice = binding.editTextBasePrice.text.toString().toDouble(),
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
            amount = binding.editTextAmount.text.toString().toDouble(),
            unit = binding.spinnerUnit.selectedItemId.toInt()
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
        binding.spinnerUnit.adapter =
            ArrayAdapter(requireContext(), R.layout.spinner_item_view, R.id.itemTextView, Unit.getArrayOfStrings(requireContext()))

        binding.spinnerDishType.adapter =
            ArrayAdapter(requireContext(), R.layout.spinner_item_view, R.id.itemTextView, DishType.getArrayOfStrings(requireContext()))
    }

    fun getIngredientListAndSetIngredientButtons() {
        val databaseRef = Firebase.database.getReference("ingredients").child("basic")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<HashMap<String, IngredientBasic>>() ?: return
                allIngredients = data.toList().map { it.second }.toMutableList()
                setIngredientViews()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun setIngredientViews() {
        val list = allIngredients.map { it.name }.toMutableList()

        setAddIngredientButton(
            binding.buttonAddBaseIngredient, list, baseIngredientsList, binding.recyclerViewBaseIngredients
        )
        setAddIngredientButton(
            binding.buttonAddOtherIngredient, list, otherIngredientsList, binding.recyclerViewOtherIngredients
        )
        setAddIngredientButton(
            binding.buttonAddPossibleIngredient, list, possibleIngredientsList, binding.recyclerViewPossibleIngredients
        )

        binding.recyclerViewBaseIngredients.adapter =
            DishIngredientsRecyclerAdapter(baseIngredientsList, this, 0)
        SubItemUtils.setRecyclerSize(binding.recyclerViewBaseIngredients, baseIngredientsList.size, requireContext())

        binding.recyclerViewOtherIngredients.adapter =
            DishIngredientsRecyclerAdapter(otherIngredientsList, this, 1)
        SubItemUtils.setRecyclerSize(binding.recyclerViewOtherIngredients, otherIngredientsList.size, requireContext())

        binding.recyclerViewPossibleIngredients.adapter =
            DishIngredientsRecyclerAdapter(possibleIngredientsList, this, 2)
        SubItemUtils.setRecyclerSize(binding.recyclerViewPossibleIngredients, possibleIngredientsList.size, requireContext())
    }

    private fun setAddIngredientButton(
        button: Button,
        list: List<String>,
        recyclerList: MutableList<IngredientItem>,
        recyclerView: RecyclerView
    ) {
        button.setOnClickListener(
            IngredientModifyDishOnClickListener(
                requireContext(),
                list,
                recyclerList,
                recyclerView,
                allIngredients,
                this
            )
        )
    }

    fun getAllergenListAndSetAllergenButtons() {
        val databaseRef = Firebase.database.getReference("allergens").child("basic")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<HashMap<String, AllergenBasic>>() ?: return
                allAllergens = data.toList().map { it.second }.toMutableList()
                setAllergenViews()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun setAllergenViews() {
        binding.buttonAddAllergen.setOnClickListener(
            AllergenModifyDishOnClickListener(
                requireContext(),
                allAllergens.map { it.name }.toMutableList(),
                allergensList,
                allAllergens,
                this
            )
        )

        binding.recyclerViewAllergens.adapter =
            DishAllergensRecyclerAdapter(allergensList, this)
        SubItemUtils.setRecyclerSize(binding.recyclerViewAllergens, allergensList.size, requireContext())
    }

    fun checkIfIngredientItemExists(ingredientId: String): Boolean {
        return baseIngredientsList.map { it.id }.contains(ingredientId)
                || otherIngredientsList.map { it.id }.contains(ingredientId)
                || possibleIngredientsList.map { it.id }.contains(ingredientId)
    }

    fun changeIngredientItemState(
        item: MenuItem,
        ingredientItem: IngredientItem,
        originalListId: Int
    ) {
        val targetState = when (item.itemId) {
            R.id.setBase -> IngredientItemState.BASE
            R.id.setOther -> IngredientItemState.OTHER
            R.id.setPossible -> IngredientItemState.POSSIBLE
            R.id.changeAmount -> IngredientItemState.CHANGE_AMOUNT
            R.id.changeExtraPrice -> IngredientItemState.CHANGE_EXTRA_PRICE
            else -> IngredientItemState.REMOVE
        }

        if (originalListId == targetState.ordinal) {
            return
        }

        val lists = arrayListOf(baseIngredientsList, otherIngredientsList, possibleIngredientsList)
        val recyclers = arrayListOf(
            binding.recyclerViewBaseIngredients,
            binding.recyclerViewOtherIngredients,
            binding.recyclerViewPossibleIngredients
        )

        if (IngredientItemState.isActionRemove(targetState)) {
            SubItemUtils.removeIngredientItem(lists[originalListId], recyclers[originalListId], ingredientItem, requireContext())
            ingredientItem.extraPrice = 0.0
        }
        if (IngredientItemState.isActionAdd(targetState)) {
            SubItemUtils.addIngredientItem(lists[targetState.ordinal], recyclers[targetState.ordinal], ingredientItem, requireContext())
        }
        if (targetState == IngredientItemState.CHANGE_AMOUNT) {
            SubItemUtils.addChangeIngredientItemAmountDialog(recyclers[originalListId], ingredientItem, requireContext())
        }
        if (targetState == IngredientItemState.CHANGE_EXTRA_PRICE) {
            if (originalListId != 2) {
                Toast.makeText(context, getString(R.string.only_possible_have_extra_price), Toast.LENGTH_SHORT).show()
            } else {
                SubItemUtils.addChangeIngredientExtraPriceDialog(recyclers[originalListId], ingredientItem, requireContext())
            }
        }
    }

    // TODO problemy z mutable list
    fun removeAllergenItem(allergenItem: AllergenBasic) {
        allergensList.remove(allergenItem)
        binding.recyclerViewAllergens.adapter?.notifyDataSetChanged() //TODO Zła praktyka
        SubItemUtils.setRecyclerSize(binding.recyclerViewAllergens, allergensList.size, requireContext())
    }

    fun addAllergenItem(allergenItem: AllergenBasic) {
        allergensList.add(allergenItem)
        binding.recyclerViewAllergens.adapter?.notifyDataSetChanged() //TODO Zła praktyka
        SubItemUtils.setRecyclerSize(binding.recyclerViewAllergens, allergensList.size, requireContext())
    }
}