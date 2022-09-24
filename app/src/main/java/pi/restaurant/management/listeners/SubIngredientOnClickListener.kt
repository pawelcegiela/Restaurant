package pi.restaurant.management.listeners

import android.app.Dialog
import android.content.Context
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.R
import pi.restaurant.management.data.Ingredient
import pi.restaurant.management.data.IngredientItem
import pi.restaurant.management.fragments.ingredients.AbstractModifyIngredientFragment
import pi.restaurant.management.utils.SubItemUtils

class SubIngredientOnClickListener(
    context: Context,
    list: List<String>,
    private val recyclerList: MutableList<IngredientItem>,
    private val recyclerView: RecyclerView,
    private val allIngredients: MutableList<Ingredient>,
    private val fragment: AbstractModifyIngredientFragment
) :
    AbstractModifyDishOnClickListener(context, list) {

    override fun setOnItemClickListener(dialog: Dialog, listView: ListView) {
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                if (recyclerList.map { it.id }.contains(allIngredients[position].id)) {
                    Toast.makeText(
                        fragment.activity,
                        fragment.getString(R.string.ingredient_already_added),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val item =
                        IngredientItem(
                            id = allIngredients[position].id,
                            name = allIngredients[position].name,
                            unit = allIngredients[position].unit
                        )
                    SubItemUtils.addIngredientItem(recyclerList, recyclerView, item, fragment.context!!)
                }
                dialog.dismiss()
            }
    }
}