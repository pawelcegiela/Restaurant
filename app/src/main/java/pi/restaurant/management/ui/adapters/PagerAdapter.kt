package pi.restaurant.management.ui.adapters

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
import pi.restaurant.management.ui.activities.*
import pi.restaurant.management.ui.fragments.ItemListSubFragment
import pi.restaurant.management.ui.fragments.allergens.AllergensItemListSubFragment
import pi.restaurant.management.ui.fragments.discounts.DiscountsItemListSubFragment
import pi.restaurant.management.ui.fragments.dishes.DishesItemListSubFragment
import pi.restaurant.management.ui.fragments.dishes.DishesMainFragment
import pi.restaurant.management.ui.fragments.ingredients.IngredientsItemListSubFragment
import pi.restaurant.management.ui.fragments.orders.OrdersItemListSubFragment
import pi.restaurant.management.ui.fragments.workers.WorkersItemListSubFragment

@Suppress("UNCHECKED_CAST")
class PagerAdapter<Tab>(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val data: Array<Tab>,
    private val activity: FragmentActivity,
    private val parentFragment: Fragment,
    private val list: MutableList<AbstractDataObject>?,
    private val fabFilter: FloatingActionButton
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return data.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (activity) {
            is AllergensActivity -> AllergensItemListSubFragment(list as MutableList<AllergenBasic>, fabFilter)
            is DiscountsActivity -> DiscountsItemListSubFragment(list as MutableList<DiscountBasic>, position, fabFilter)
            is DishesActivity -> DishesItemListSubFragment(list as MutableList<DishBasic>, position, fabFilter, true)
            is IngredientsActivity -> IngredientsItemListSubFragment(list as MutableList<IngredientBasic>, position, fabFilter)
            is OrdersActivity -> {
                if (parentFragment is DishesMainFragment) DishesItemListSubFragment(
                    list as MutableList<DishBasic>,
                    position,
                    fabFilter,
                    false
                )
                else OrdersItemListSubFragment(list as MutableList<OrderBasic>, position, fabFilter)
            }
            is WorkersActivity -> WorkersItemListSubFragment(list as MutableList<UserBasic>, position, fabFilter)
            else -> ItemListSubFragment(fabFilter)
        }
    }
}