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
import pi.restaurant.management.utils.SubItemUtils


abstract class AbstractModifyDishFragment : AbstractModifyItemFragment() {

    // TODO Change Recycler to ListView!

    private var _binding: FragmentModifyDishBinding? = null
    val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val saveButton get() = binding.buttonSave
    override val removeButton get() = binding.buttonRemove
    override var itemId = ""

    var baseIngredientsList: MutableList<IngredientItem> = ArrayList()
    var otherIngredientsList: MutableList<IngredientItem> = ArrayList()
    var possibleIngredientsList: MutableList<IngredientItem> = ArrayList()
    var allergensList: MutableList<Allergen> = ArrayList()
    lateinit var allIngredients: MutableList<Ingredient>
    lateinit var allAllergens: MutableList<Allergen>

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
        initializeRecyclerViews()
        setSaveButtonListener()
        getIngredientListAndSetIngredientButtons()
        getAllergenListAndSetAllergenButtons()
    }

    override fun getDataObject(): AbstractDataObject {
        val baseIngredients = HashMap(baseIngredientsList.associateBy { it.id })
        val otherIngredients = HashMap(otherIngredientsList.associateBy { it.id })
        val possibleIngredients = HashMap(possibleIngredientsList.associateBy { it.id })
        val allergens = HashMap(allergensList.associateBy { it.id })

        val discountPrice = if (binding.checkBoxDiscount.isChecked) {
            binding.editTextDiscountPrice.text.toString().toDouble()
        } else {
            0.0
        }
        return Dish(
            id = itemId,
            name = binding.editTextName.text.toString(),
            description = binding.editTextDescription.text.toString(),
            isActive = binding.checkBoxActive.isChecked,
            basePrice = binding.editTextBasePrice.text.toString().toDouble(),
            isDiscounted = binding.checkBoxDiscount.isChecked,
            discountPrice = discountPrice,
            baseIngredients = baseIngredients,
            otherIngredients = otherIngredients,
            possibleIngredients = possibleIngredients,
            allergens = allergens,
            dishType = binding.spinnerDishType.selectedItemId.toInt(),
            amount = binding.editTextAmount.text.toString().toDouble(),
            unit = binding.spinnerUnit.selectedItemId.toInt()
        )
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
            ArrayAdapter(context!!, R.layout.spinner_item_view, R.id.itemTextView, Unit.getArrayOfStrings(context!!))

        binding.spinnerDishType.adapter =
            ArrayAdapter(context!!, R.layout.spinner_item_view, R.id.itemTextView, DishType.getArrayOfStrings(context!!))
    }

    fun initializeRecyclerViews() {
        binding.recyclerViewBaseIngredients.adapter =
            DishIngredientsRecyclerAdapter(baseIngredientsList, this, 0)

        binding.recyclerViewOtherIngredients.adapter =
            DishIngredientsRecyclerAdapter(otherIngredientsList, this, 1)

        binding.recyclerViewPossibleIngredients.adapter =
            DishIngredientsRecyclerAdapter(possibleIngredientsList, this, 2)

        binding.recyclerViewAllergens.adapter =
            DishAllergensRecyclerAdapter(allergensList, this)
    }

    fun getIngredientListAndSetIngredientButtons() {
        val databaseRef = Firebase.database.getReference("ingredients")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<HashMap<String, Ingredient>>() ?: return
                allIngredients = data.toList().map { it.second }.toMutableList()
                setIngredientsButtons()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun setIngredientsButtons() {
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
    }

    private fun setAddIngredientButton(
        button: Button,
        list: List<String>,
        recyclerList: MutableList<IngredientItem>,
        recyclerView: RecyclerView
    ) {
        SubItemUtils.setRecyclerSize(recyclerView, recyclerList.size, context!!)
        button.setOnClickListener(
            IngredientModifyDishOnClickListener(
                context!!,
                list,
                recyclerList,
                recyclerView,
                allIngredients,
                this
            )
        )
    }

    fun getAllergenListAndSetAllergenButtons() {
        val databaseRef = Firebase.database.getReference("allergens")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<HashMap<String, Allergen>>() ?: return
                allAllergens = data.toList().map { it.second }.toMutableList()
                setAllergensButton()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun setAllergensButton() {
        SubItemUtils.setRecyclerSize(binding.recyclerViewAllergens, allergensList.size, context!!)
        binding.buttonAddAllergen.setOnClickListener(
            AllergenModifyDishOnClickListener(
                context!!,
                allAllergens.map { it.name }.toMutableList(),
                allergensList,
                allAllergens,
                this
            )
        )
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
            SubItemUtils.removeIngredientItem(lists[originalListId], recyclers[originalListId], ingredientItem, context!!)
            ingredientItem.extraPrice = 0.0
        }
        if (IngredientItemState.isActionAdd(targetState)) {
            SubItemUtils.addIngredientItem(lists[targetState.ordinal], recyclers[targetState.ordinal], ingredientItem, context!!)
        }
        if (targetState == IngredientItemState.CHANGE_AMOUNT) {
            SubItemUtils.addChangeIngredientItemAmountDialog(recyclers[originalListId], ingredientItem, context!!)
        }
        if (targetState == IngredientItemState.CHANGE_EXTRA_PRICE) {
            if (originalListId != 2) {
                Toast.makeText(context, getString(R.string.only_possible_have_extra_price), Toast.LENGTH_SHORT).show()
            } else {
                SubItemUtils.addChangeIngredientExtraPriceDialog(recyclers[originalListId], ingredientItem, context!!)
            }
        }
    }

    // TODO problemy z mutable list
    fun removeAllergenItem(allergenItem: Allergen) {
        allergensList.remove(allergenItem)
        binding.recyclerViewAllergens.adapter?.notifyDataSetChanged() //TODO Zła praktyka
        SubItemUtils.setRecyclerSize(binding.recyclerViewAllergens, allergensList.size, context!!)
    }

    fun addAllergenItem(allergenItem: Allergen) {
        allergensList.add(allergenItem)
        binding.recyclerViewAllergens.adapter?.notifyDataSetChanged() //TODO Zła praktyka
        SubItemUtils.setRecyclerSize(binding.recyclerViewAllergens, allergensList.size, context!!)
    }
}