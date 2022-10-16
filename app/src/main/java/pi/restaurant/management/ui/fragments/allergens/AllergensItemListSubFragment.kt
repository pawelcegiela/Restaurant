package pi.restaurant.management.ui.fragments.allergens

import android.os.Bundle
import android.view.View
import pi.restaurant.management.objects.data.allergen.AllergenBasic
import pi.restaurant.management.ui.adapters.AllergensRecyclerAdapter
import pi.restaurant.management.ui.fragments.ItemListSubFragment

class AllergensItemListSubFragment(private val list: MutableList<AllergenBasic>) : ItemListSubFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = AllergensRecyclerAdapter(list, this)
    }
}