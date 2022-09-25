package pi.restaurant.management.fragments.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.adapters.DishIngredientsRecyclerAdapter
import pi.restaurant.management.data.*
import pi.restaurant.management.databinding.FragmentModifyIngredientBinding
import pi.restaurant.management.enums.IngredientItemState
import pi.restaurant.management.enums.Unit
import pi.restaurant.management.fragments.AbstractModifyItemFragment
import pi.restaurant.management.listeners.SubIngredientOnClickListener
import pi.restaurant.management.utils.StringFormatUtils
import pi.restaurant.management.utils.SubItemUtils


abstract class AbstractModifyIngredientFragment : AbstractModifyItemFragment() {

    private var _binding: FragmentModifyIngredientBinding? = null
    val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val saveButton get() = binding.buttonSave
    override val removeButton get() = binding.buttonRemove
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
        setSaveButtonListener()
        setCheckBoxListener()
    }

    private fun initializeSpinner() {
        binding.spinnerUnit.adapter =
            ArrayAdapter(requireContext(), R.layout.spinner_item_view, R.id.itemTextView, Unit.getArrayOfStrings(requireContext()))
    }

    private fun setCheckBoxListener() {
        binding.checkBoxSubDish.setOnCheckedChangeListener { _, isChecked ->
            setSubDish(isChecked)
        }
    }

    private fun setSubDish(isSubDish: Boolean) {
        binding.recyclerViewSubIngredients.visibility = if (isSubDish) View.VISIBLE else View.GONE
        binding.buttonAddSubIngredient.visibility = if (isSubDish) View.VISIBLE else View.GONE
    }

    fun getIngredientListAndSetIngredientButton() {
        val databaseRef = Firebase.database.getReference("ingredients").child("basic")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<HashMap<String, IngredientBasic>>() ?: return
                allIngredients = data.toList().map { it.second }.toMutableList()
                SubItemUtils.setRecyclerSize(binding.recyclerViewSubIngredients, subIngredientsList.size, context!!)
                binding.buttonAddSubIngredient.setOnClickListener(
                    SubIngredientOnClickListener(
                        context!!,
                        allIngredients.map { it.name }.toMutableList(),
                        subIngredientsList,
                        binding.recyclerViewSubIngredients,
                        allIngredients,
                        this@AbstractModifyIngredientFragment
                    )
                )
                binding.recyclerViewSubIngredients.adapter =
                    DishIngredientsRecyclerAdapter(subIngredientsList, this@AbstractModifyIngredientFragment, 0)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun changeIngredientItemState(item: MenuItem, ingredientItem: IngredientItem) {
        val targetState = when (item.itemId) {
            R.id.changeAmount -> IngredientItemState.CHANGE_AMOUNT
            else -> IngredientItemState.REMOVE
        }

        if (targetState == IngredientItemState.REMOVE) {
            SubItemUtils.removeIngredientItem(subIngredientsList, binding.recyclerViewSubIngredients, ingredientItem, requireContext())
        }
        if (targetState == IngredientItemState.CHANGE_AMOUNT) {
            SubItemUtils.addChangeIngredientItemAmountDialog(binding.recyclerViewSubIngredients, ingredientItem, requireContext())
        }
    }

    override fun getDataObject(): SplitDataObject {
        itemId = itemId.ifEmpty { StringFormatUtils.formatId() }

        val basic = IngredientBasic(
            id = itemId,
            name = binding.editTextName.text.toString(),
            amount = binding.editTextAmount.text.toString().toInt(),
            unit = binding.spinnerUnit.selectedItemPosition,
            subDish = binding.checkBoxSubDish.isChecked,
        )
        val details = IngredientDetails(
            id = itemId,
            subIngredients = if (binding.checkBoxSubDish.isChecked) subIngredientsList else null
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