package pi.restaurant.management.listeners

import android.app.Dialog
import android.widget.*
import androidx.fragment.app.Fragment
import pi.restaurant.management.R
import pi.restaurant.management.data.IngredientBasic
import pi.restaurant.management.data.IngredientItem
import pi.restaurant.management.enums.IngredientStatus
import pi.restaurant.management.views.DialogIngredientProperties

class AddIngredientButtonListener(
    private val recyclerList: MutableList<IngredientItem>,
    private val allIngredients: MutableList<IngredientBasic>,
    private val fragment: Fragment
) :
    AbstractAddSubItemButtonListener(fragment.requireContext(), allIngredients.map { it.name }.toMutableList()) {

    override fun setOnItemClickListener(dialog: Dialog, listView: ListView) {
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val list = allIngredients.filter {
                    it.name.lowercase().contains(dialog.findViewById<EditText>(R.id.editTextSearch).text.toString().lowercase())
                }
                if (recyclerList.map { it.id }.contains(list[position].id)) {
                    Toast.makeText(
                        fragment.activity,
                        fragment.getString(R.string.ingredient_already_added),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val ingredientItem = IngredientItem(list[position].id, list[position].name, list[position].unit)
                    DialogIngredientProperties(fragment, Pair(ingredientItem, IngredientStatus.BASE), true)
                }
                dialog.dismiss()
            }
    }
}