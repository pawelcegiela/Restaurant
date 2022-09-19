package pi.restaurant.management.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.R
import pi.restaurant.management.data.Allergen
import pi.restaurant.management.data.IngredientItem
import pi.restaurant.management.databinding.ItemDishIngredientBinding
import pi.restaurant.management.fragments.dishes.AbstractModifyDishFragment
import pi.restaurant.management.utils.StringFormatUtils


class DishAllergensRecyclerAdapter(
    private val dataSet: List<Allergen>,
    private val fragment: AbstractModifyDishFragment,
) :
    RecyclerView.Adapter<DishAllergensRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: ItemDishIngredientBinding,
        val context: Context,
        val fragment: AbstractModifyDishFragment,
        private val dataSet: List<Allergen>,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            setButtonListener()
        }

        private fun setButtonListener() {
            binding.buttonOptions.text = fragment.getString(R.string.remove)
            binding.buttonOptions.setOnClickListener {
                fragment.removeAllergenItem(dataSet[layoutPosition])
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDishIngredientBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment, dataSet)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewName.text = dataSet[position].name
    }

    override fun getItemCount() = dataSet.size

}

