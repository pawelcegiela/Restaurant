package pi.restaurantapp.ui.dialogs

import android.widget.AdapterView
import androidx.fragment.app.Fragment
import pi.restaurantapp.objects.data.ingredient.IngredientBasic
import pi.restaurantapp.objects.data.ingredient.IngredientItem
import pi.restaurantapp.objects.enums.IngredientStatus

class AddIngredientDialog(
    private var fragment: Fragment,
    private val recyclerList: MutableList<IngredientItem>,
    private val allIngredients: MutableList<IngredientBasic>,
    title: String
) :
    AbstractItemListDialog(
        fragment.requireContext(),
        allIngredients.filter { !recyclerList.map { it1 -> it1.id }.contains(it.id) }.map { it.name }.toMutableList(),
        title
    ) {

    init {
        initialize()
    }

    override fun setListener() {
        binding.listViewItems.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val list = allIngredients.filter { !recyclerList.map { it1 -> it1.id }.contains(it.id) }.filter {
                    it.name.lowercase().contains(binding.editTextSearch.text.toString().lowercase())
                }
                val ingredientItem = IngredientItem(list[position].id, list[position].name, list[position].unit)
                IngredientPropertiesDialog(fragment, Pair(ingredientItem, IngredientStatus.BASE), true)
                dismiss()
            }
    }
}