package pi.restaurant.management.listeners

import android.app.Dialog
import android.content.Context
import android.widget.*
import pi.restaurant.management.R
import pi.restaurant.management.data.AllergenBasic
import pi.restaurant.management.fragments.dishes.AbstractModifyDishFragment

class AddAllergenButtonListener(
    context: Context,
    list: List<String>,
    private val allergensList: MutableList<AllergenBasic>,
    private val allAllergens: MutableList<AllergenBasic>,
    private val fragment: AbstractModifyDishFragment
) :
    AbstractAddSubItemButtonListener(context, list) {

    override fun setOnItemClickListener(dialog: Dialog, listView: ListView) {
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val list = allAllergens.filter {
                    it.name.lowercase().contains(dialog.findViewById<EditText>(R.id.editTextSearch).text.toString().lowercase())
                }
                if (allergensList.map { it.id }.contains(list[position].id)) {
                    Toast.makeText(
                        fragment.activity,
                        fragment.getString(R.string.allergen_already_added),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val item = list[position]
                    fragment.addAllergenItem(item)
                }
                dialog.dismiss()
            }
    }
}