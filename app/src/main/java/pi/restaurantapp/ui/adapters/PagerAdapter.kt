package pi.restaurantapp.ui.adapters

import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.objects.data.chat.ChatInfo
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.data.dish.DishBasic
import pi.restaurantapp.objects.data.ingredient.IngredientBasic
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.ui.activities.client.ClientNewOrderActivity
import pi.restaurantapp.ui.activities.client.ClientOrdersActivity
import pi.restaurantapp.ui.activities.management.*
import pi.restaurantapp.ui.fragments.ItemListSubFragment
import pi.restaurantapp.ui.fragments.client.orders.ClientOrdersItemListSubFragment
import pi.restaurantapp.ui.fragments.management.allergens.AllergensItemListSubFragment
import pi.restaurantapp.ui.fragments.management.chats.ChatsItemListSubFragment
import pi.restaurantapp.ui.fragments.management.customers.CustomersItemListSubFragment
import pi.restaurantapp.ui.fragments.management.discounts.DiscountsItemListSubFragment
import pi.restaurantapp.ui.fragments.management.dishes.DishesItemListSubFragment
import pi.restaurantapp.ui.fragments.management.dishes.DishesMainFragment
import pi.restaurantapp.ui.fragments.management.ingredients.IngredientsItemListSubFragment
import pi.restaurantapp.ui.fragments.management.orders.OrdersItemListSubFragment
import pi.restaurantapp.ui.fragments.management.workers.WorkersItemListSubFragment

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
            is ClientOrdersActivity -> ClientOrdersItemListSubFragment(list as MutableList<OrderBasic>, position, fabFilter, searchView)
            is ClientNewOrderActivity -> DishesItemListSubFragment(list as MutableList<DishBasic>, position, fabFilter, searchView, false)
            is ChatsActivity -> ChatsItemListSubFragment(list as MutableList<ChatInfo>, fabFilter, searchView)
            is CustomersActivity -> CustomersItemListSubFragment(list as MutableList<UserBasic>, fabFilter, searchView)
            else -> ItemListSubFragment(fabFilter, searchView)
        }
    }
}