package pi.restaurantapp.ui.dialogs

import android.content.Context
import android.widget.AdapterView
import pi.restaurantapp.objects.data.allergen.AllergenBasic

/**
 * Dialog with a list of allergens to add.
 */
class AddAllergenDialog(
    fragmentContext: Context,
    private val allergensList: MutableList<AllergenBasic>,
    private val allAllergens: MutableList<AllergenBasic>,
    title: String,
    private val onItemChosen: (AllergenBasic) -> (Unit)
) :
    AbstractItemListDialog(
        fragmentContext,
        allAllergens.filter { !allergensList.map { it1 -> it1.id }.contains(it.id) }.map { it.name }.toMutableList(),
        title
    ) {

    init {
        initialize()
    }

    override fun setListener() {
        binding.listViewItems.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val list = allAllergens.filter { !allergensList.map { it1 -> it1.id }.contains(it.id) }.filter {
                    it.name.lowercase().contains(binding.editTextSearch.text.toString().lowercase())
                }
                onItemChosen(list[position])
                dismiss()
            }
    }
}