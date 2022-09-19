package pi.restaurant.management.listeners

import android.app.Dialog
import android.content.Context
import android.widget.*
import pi.restaurant.management.R
import pi.restaurant.management.data.Allergen
import pi.restaurant.management.fragments.dishes.AbstractModifyDishFragment

class AllergenModifyDishOnClickListener(
    context: Context,
    list: List<String>,
    private val allergensList: MutableList<Allergen>,
    private val allAllergens: MutableList<Allergen>,
    private val fragment: AbstractModifyDishFragment
) :
    AbstractModifyDishOnClickListener(context, list) {

    override fun setOnItemClickListener(dialog: Dialog, listView: ListView) {
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                if (allergensList.map { it.id }.contains(allAllergens[position].id)) {
                    Toast.makeText(
                        fragment.activity,
                        fragment.getString(R.string.allergen_already_added),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val item = allAllergens[position]
                    fragment.addAllergenItem(item)
                }
                dialog.dismiss()
            }
    }
}