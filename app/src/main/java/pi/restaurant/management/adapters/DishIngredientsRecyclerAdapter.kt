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
import pi.restaurant.management.databinding.ItemSubItemBinding
import pi.restaurant.management.enums.IngredientItemState
import pi.restaurant.management.enums.IngredientStatus
import pi.restaurant.management.fragments.dishes.AbstractModifyDishFragment
import pi.restaurant.management.fragments.ingredients.AbstractModifyIngredientFragment
import pi.restaurant.management.fragments.orders.CustomizeDishFragment
import pi.restaurant.management.utils.StringFormatUtils


class DishIngredientsRecyclerAdapter(
    private val dataSet: List<IngredientItem>,
    private val fragment: Fragment,
    private val list: IngredientStatus
) :
    RecyclerView.Adapter<DishIngredientsRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: ItemSubItemBinding,
        val context: Context,
        val fragment: Fragment,
        private val dataSet: List<IngredientItem>,
        private val list: IngredientStatus
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            initialize()
        }

        private fun initialize() {
            when (fragment) {
                is AbstractModifyDishFragment -> {
                    binding.buttonOptions.setOnClickListener {
                        fragment.changeIngredientProperties(dataSet[layoutPosition], list)
                    }
                }
                is AbstractModifyIngredientFragment -> {
                    binding.buttonOptions.setOnClickListener {
                        fragment.changeSubIngredientProperties(dataSet[layoutPosition])
                    }
                }
                is CustomizeDishFragment -> {
                    when (list) {
                        IngredientStatus.BASE -> binding.buttonOptions.visibility = View.GONE
                        IngredientStatus.OTHER -> binding.buttonOptions.text = context.getString(R.string.remove)
                        IngredientStatus.POSSIBLE -> binding.buttonOptions.text = context.getString(R.string.add)
                    }

                    binding.buttonOptions.setOnClickListener {
                        if (list == IngredientStatus.OTHER) {
                            fragment.changeIngredientItemState(IngredientItemState.POSSIBLE, dataSet[layoutPosition])
                        } else if (list == IngredientStatus.POSSIBLE) {
                            fragment.changeIngredientItemState(IngredientItemState.REMOVE, dataSet[layoutPosition])
                        }
                    }
                }
                else -> {
                    binding.buttonOptions.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSubItemBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment, dataSet, list)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewName.text = "${dataSet[position].name} [${
            StringFormatUtils.formatAmountWithUnit(
                viewHolder.context,
                dataSet[position].amount,
                dataSet[position].unit
            )
        }" + if (list == IngredientStatus.POSSIBLE || dataSet[position].extraPrice != 0.0) ",  + ${dataSet[position].extraPrice} zł]" else "]"
    }

    override fun getItemCount() = dataSet.size

}

