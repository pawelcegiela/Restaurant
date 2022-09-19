package pi.restaurant.management.fragments.dishes

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import pi.restaurant.management.enums.IngredientItemState
import pi.restaurant.management.fragments.AbstractModifyItemFragment
import pi.restaurant.management.listeners.AllergenModifyDishOnClickListener
import pi.restaurant.management.listeners.IngredientModifyDishOnClickListener


abstract class AbstractModifyDishFragment : AbstractModifyItemFragment() {

    // TODO Change Recycler to ListView!

    private var _binding: FragmentModifyDishBinding? = null
    val binding get() = _binding!!

    override val databasePath = "dishes"
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
        val spinnerUnit: Spinner = binding.spinnerUnit
        ArrayAdapter.createFromResource(
            context!!,
            R.array.units,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerUnit.adapter = adapter
        }

        val spinnerDishType: Spinner = binding.spinnerDishType
        ArrayAdapter.createFromResource(
            context!!,
            R.array.dish_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDishType.adapter = adapter
        }
    }

    fun initializeRecyclerViews() {
        binding.recyclerViewBaseIngredients.adapter =
            DishIngredientsRecyclerAdapter(baseIngredientsList, this@AbstractModifyDishFragment, 0)

        binding.recyclerViewOtherIngredients.adapter =
            DishIngredientsRecyclerAdapter(otherIngredientsList, this@AbstractModifyDishFragment, 1)

        binding.recyclerViewPossibleIngredients.adapter =
            DishIngredientsRecyclerAdapter(possibleIngredientsList, this@AbstractModifyDishFragment, 2)

        binding.recyclerViewAllergens.adapter =
            DishAllergensRecyclerAdapter(allergensList, this@AbstractModifyDishFragment)
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
        setRecyclerSize(recyclerView, recyclerList.size)
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
        setRecyclerSize(binding.recyclerViewAllergens, allergensList.size)
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

    private fun setRecyclerSize(recyclerView: RecyclerView, size: Int) {
        val itemSize = 60
        val layoutParams = LinearLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            (itemSize * context!!.resources.displayMetrics.density * size).toInt()
        )
        recyclerView.layoutParams = layoutParams
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
            removeItemFromList(lists[originalListId], recyclers[originalListId], ingredientItem)
        }
        if (IngredientItemState.isActionAdd(targetState)) {
            addItemToList(lists[targetState.ordinal], recyclers[targetState.ordinal], ingredientItem)
        }
        if (targetState == IngredientItemState.CHANGE_AMOUNT) {
            addChangeIngredientItemAmountDialog(recyclers[originalListId], ingredientItem)
        }
    }

    private fun addChangeIngredientItemAmountDialog(recyclerView: RecyclerView, item: IngredientItem) {
        val dialog = Dialog(context!!)

        dialog.setContentView(R.layout.dialog_dish_ingredient_amount)
        dialog.window!!.setLayout(1000, 1000)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val editText = dialog.findViewById<EditText>(R.id.editTextAmount)
        val button = dialog.findViewById<Button>(R.id.buttonEnter)

        editText.setText(item.amount.toString())
        button.setOnClickListener {
            item.amount = if (editText.text.isEmpty()) 0.0 else editText.text.toString().toDouble()
            dialog.dismiss()
            recyclerView.adapter?.notifyDataSetChanged() //TODO Zła praktyka
        }
    }

    private fun removeItemFromList(list: MutableList<IngredientItem>, recyclerView: RecyclerView, item: IngredientItem) {
        list.remove(item)
        recyclerView.adapter?.notifyDataSetChanged() //TODO Zła praktyka
        setRecyclerSize(recyclerView, list.size)
    }

    fun addItemToList(list: MutableList<IngredientItem>, recyclerView: RecyclerView, item: IngredientItem) {
        list.add(item)
        recyclerView.adapter?.notifyDataSetChanged() //TODO Zła praktyka
        setRecyclerSize(recyclerView, list.size)
    }

    // TODO problemy z mutable list
    fun removeAllergenItem(allergenItem: Allergen) {
        allergensList.remove(allergenItem)
        binding.recyclerViewAllergens.adapter?.notifyDataSetChanged() //TODO Zła praktyka
        setRecyclerSize(binding.recyclerViewAllergens, allergensList.size)
    }

    fun addAllergenItem(allergenItem: Allergen) {
        allergensList.add(allergenItem)
        binding.recyclerViewAllergens.adapter?.notifyDataSetChanged() //TODO Zła praktyka
        setRecyclerSize(binding.recyclerViewAllergens, allergensList.size)
    }
}