package pi.restaurant.management.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.R
import pi.restaurant.management.data.IngredientItem
import pi.restaurant.management.databinding.ItemDishIngredientBinding
import pi.restaurant.management.fragments.dishes.AbstractModifyDishFragment
import pi.restaurant.management.utils.StringFormatUtils


class DishIngredientsRecyclerAdapter(
    private val dataSet: List<IngredientItem>,
    private val fragment: Fragment,
    private val listId: Int
) :
    RecyclerView.Adapter<DishIngredientsRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: ItemDishIngredientBinding,
        val context: Context,
        val fragment: Fragment,
        private val dataSet: List<IngredientItem>,
        private val listId: Int
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            initialize()
        }

        private fun initialize() {
            if (fragment is AbstractModifyDishFragment) {
                binding.buttonOptions.setOnClickListener {
                    val popup = PopupMenu(context, binding.buttonOptions)
                    popup.menuInflater.inflate(R.menu.popup_menu_dish_ingredient, popup.menu)

                    popup.setOnMenuItemClickListener { item ->
                        fragment.changeIngredientItemState(item, dataSet[layoutPosition], listId)
                        true
                    }

                    popup.show()
                }
            } else {
                binding.buttonOptions.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDishIngredientBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment, dataSet, listId)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewName.text = "${dataSet[position].name} [${
            StringFormatUtils.formatAmountWithUnit(
                viewHolder.context,
                dataSet[position].amount,
                dataSet[position].unit
            )
        }]"
    }

    override fun getItemCount() = dataSet.size

}

