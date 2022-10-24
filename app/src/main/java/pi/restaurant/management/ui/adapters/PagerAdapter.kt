package pi.restaurant.management.ui.adapters

import android.util.Log
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pi.restaurant.management.objects.data.AbstractDataObject
import pi.restaurant.management.objects.data.allergen.AllergenBasic
import pi.restaurant.management.objects.data.discount.DiscountBasic
import pi.restaurant.management.objects.data.dish.DishBasic
import pi.restaurant.management.objects.data.ingredient.IngredientBasic
import pi.restaurant.management.objects.data.order.OrderBasic
import pi.restaurant.management.objects.data.user.UserBasic
import pi.restaurant.management.ui.activities.management.*
import pi.restaurant.management.ui.fragments.management.ItemListSubFragment
import pi.restaurant.management.ui.fragments.management.allergens.AllergensItemListSubFragment
import pi.restaurant.management.ui.fragments.management.discounts.DiscountsItemListSubFragment
import pi.restaurant.management.ui.fragments.management.dishes.DishesItemListSubFragment
import pi.restaurant.management.ui.fragments.management.dishes.DishesMainFragment
import pi.restaurant.management.ui.fragments.management.ingredients.IngredientsItemListSubFragment
import pi.restaurant.management.ui.fragments.management.orders.OrdersItemListSubFragment
import pi.restaurant.management.ui.fragments.management.workers.WorkersItemListSubFragment

@Suppress("UNCHECKED_CAST")
class PagerAdapter<Tab>(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val data: Array<Tab>,
    private val activity: FragmentActivity,
    private val parentFragment: Fragment,
    private val list: MutableList<AbstractDataObject>?,
    private val fabFilter: FloatingActionButton,
    private val searchView: SearchView
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return data.size
    }

    override fun createFragment(position: Int): Fragment {
        Log.e("has Search View", searchView.id.toString())
        return when (activity) {
            is AllergensActivity -> AllergensItemListSubFragment(list as MutableList<AllergenBasic>, fabFilter, searchView)
            is DiscountsActivity -> DiscountsItemListSubFragment(list as MutableList<DiscountBasic>, position, fabFilter, searchView)
            is DishesActivity -> DishesItemListSubFragment(list as MutableList<DishBasic>, position, fabFilter, searchView, true)
            is IngredientsActivity -> IngredientsItemListSubFragment(list as MutableList<IngredientBasic>, position, fabFilter, searchView)
            is OrdersActivity -> {
                if (parentFragment is DishesMainFragment) {
                    DishesItemListSubFragment(list as MutableList<DishBasic>, position, fabFilter, searchView, false)
                } else {
                    OrdersItemListSubFragment(list as MutableList<OrderBasic>, position, fabFilter, searchView)
                }
            }
            is WorkersActivity -> WorkersItemListSubFragment(list as MutableList<UserBasic>, position, fabFilter, searchView)
            else -> ItemListSubFragment(fabFilter, searchView)
        }
    }
}