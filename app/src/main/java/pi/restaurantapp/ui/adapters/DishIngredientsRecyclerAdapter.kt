package pi.restaurantapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pi.restaurantapp.R
import pi.restaurantapp.databinding.ItemSubItemBinding
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.ingredient.IngredientItem
import pi.restaurantapp.objects.enums.IngredientStatus
import pi.restaurantapp.ui.fragments.management.dishes.AbstractModifyDishFragment
import pi.restaurantapp.ui.fragments.management.ingredients.AbstractModifyIngredientFragment
import pi.restaurantapp.ui.fragments.management.orders.CustomizeDishFragment


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
                        if (list != IngredientStatus.BASE) {
                            fragment.changeIngredientItemState(list, dataSet[layoutPosition])
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
        viewHolder.binding.textViewName.text =
            StringFormatUtils.formatIngredientItemHeader(dataSet[position], list, viewHolder.context)
    }

    override fun getItemCount() = dataSet.size

    companion object {
        @JvmStatic
        fun createNew(dataSet: List<IngredientItem>, fragment: Fragment, list: IngredientStatus): DishIngredientsRecyclerAdapter {
            return DishIngredientsRecyclerAdapter(dataSet, fragment, list)
        }
    }
}

