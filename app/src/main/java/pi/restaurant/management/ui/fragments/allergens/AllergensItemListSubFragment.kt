package pi.restaurant.management.ui.fragments.allergens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pi.restaurant.management.databinding.FragmentItemListSubBinding
import pi.restaurant.management.objects.data.allergen.AllergenBasic
import pi.restaurant.management.ui.adapters.AllergensRecyclerAdapter
import pi.restaurant.management.ui.fragments.ItemListSubFragment

class AllergensItemListSubFragment(private val list: MutableList<AllergenBasic>, private val position: Int) : ItemListSubFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = AllergensRecyclerAdapter(list, this)
    }
}