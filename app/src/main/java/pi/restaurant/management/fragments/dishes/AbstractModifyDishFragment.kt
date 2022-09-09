package pi.restaurant.management.fragments.dishes

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import pi.restaurant.management.adapters.DishIngredientsRecyclerAdapter
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.Dish
import pi.restaurant.management.data.Ingredient
import pi.restaurant.management.data.IngredientItem
import pi.restaurant.management.databinding.FragmentModifyDishBinding
import pi.restaurant.management.fragments.AbstractModifyItemFragment


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
    lateinit var ingredients: MutableList<Ingredient>

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
    }

    override fun getDataObject(): AbstractDataObject {
        val baseIngredients = HashMap(baseIngredientsList.associateBy { it.id })
        val otherIngredients = HashMap(otherIngredientsList.associateBy { it.id })
        val possibleIngredients = HashMap(possibleIngredientsList.associateBy { it.id })

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
            DishIngredientsRecyclerAdapter(
                possibleIngredientsList,
                this@AbstractModifyDishFragment,
                2
            )
    }

    fun getIngredientListAndSetIngredientButtons() {
        val databaseRef = Firebase.database.getReference("ingredients")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<HashMap<String, Ingredient>>() ?: return
                ingredients = data.toList().map { it.second }.toMutableList()
                setIngredientsButtons()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun setIngredientsButtons() {
        val list = ingredients.map { it.name }.toMutableList()
        setAddIngredientButton(
            binding.buttonAddBaseIngredient,
            list,
            baseIngredientsList,
            binding.recyclerViewBaseIngredients
        )
        setAddIngredientButton(
            binding.buttonAddOtherIngredient,
            list,
            otherIngredientsList,
            binding.recyclerViewOtherIngredients
        )
        setAddIngredientButton(
            binding.buttonAddPossibleIngredient,
            list,
            possibleIngredientsList,
            binding.recyclerViewPossibleIngredients
        )
    }

    private fun setAddIngredientButton(
        button: Button,
        list: List<String>,
        recyclerList: MutableList<IngredientItem>,
        recyclerView: RecyclerView
    ) {
        setRecyclerSize(recyclerView, recyclerList.size)

        button.setOnClickListener {
            val dialog = Dialog(context!!)
            val dialogWidth = Resources.getSystem().displayMetrics.widthPixels - 200
            val dialogHeight = Resources.getSystem().displayMetrics.heightPixels - 500

            dialog.setContentView(R.layout.dialog_searchable_spinner)
            dialog.window!!.setLayout(dialogWidth, dialogHeight)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            val editText = dialog.findViewById<EditText>(R.id.editTextSearch)
            val listView = dialog.findViewById<ListView>(R.id.listViewIngredients)

            val adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, list)
            listView.adapter = adapter

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    adapter.filter.filter(s)
                }

                override fun afterTextChanged(s: Editable) {}
            })
            listView.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    if (checkIfItemExists(ingredients[position].id)) {
                        Toast.makeText(
                            activity,
                            getString(R.string.ingredient_already_added),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val item =
                            IngredientItem(ingredients[position].id, ingredients[position].name, ingredients[position].unit)
                        addItemToList(recyclerList, recyclerView, item)
                    }
                    dialog.dismiss()
                }
        }
    }

    private fun checkIfItemExists(ingredientId: String): Boolean {
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

    fun changeItemState(item: MenuItem, ingredientItem: IngredientItem, originalListId: Int) {
        val targetListId = when (item.itemId) {
            R.id.setBase -> 0
            R.id.setOther -> 1
            R.id.setPossible -> 2
            R.id.changeAmount -> 3
            else -> 4
        }

        if (originalListId == targetListId) {
            return
        }

        val lists = arrayListOf(baseIngredientsList, otherIngredientsList, possibleIngredientsList)
        val recyclers = arrayListOf(
            binding.recyclerViewBaseIngredients,
            binding.recyclerViewOtherIngredients,
            binding.recyclerViewPossibleIngredients
        )

        if (targetListId != 3) { // TODO Czytelność tego kodu (magic number)
            removeItemFromList(lists[originalListId], recyclers[originalListId], ingredientItem)
        }
        if (targetListId < 3) {
            addItemToList(lists[targetListId], recyclers[targetListId], ingredientItem)
        }
        if (targetListId == 3) {
            val dialog = Dialog(context!!)

            dialog.setContentView(R.layout.dialog_dish_ingredient_amount)
            dialog.window!!.setLayout(1000, 1000)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            val editText = dialog.findViewById<EditText>(R.id.editTextAmount)
            val button = dialog.findViewById<Button>(R.id.buttonEnter)

            editText.setText(ingredientItem.amount.toString())
            button.setOnClickListener {
                if (editText.text.isEmpty()) {
                    ingredientItem.amount = 0.0
                } else {
                    ingredientItem.amount = editText.text.toString().toDouble()
                }
                dialog.dismiss()
                recyclers[originalListId].adapter?.notifyDataSetChanged() //TODO Zła praktyka
            }
        }
    }

    private fun removeItemFromList(
        list: MutableList<IngredientItem>,
        recyclerView: RecyclerView,
        ingredientItem: IngredientItem
    ) {
        list.remove(ingredientItem)
        recyclerView.adapter?.notifyDataSetChanged() //TODO Zła praktyka
        setRecyclerSize(recyclerView, list.size)
    }

    private fun addItemToList(
        list: MutableList<IngredientItem>,
        recyclerView: RecyclerView,
        ingredientItem: IngredientItem
    ) {
        list.add(ingredientItem)
        recyclerView.adapter?.notifyDataSetChanged() //TODO Zła praktyka
        setRecyclerSize(recyclerView, list.size)
    }
}