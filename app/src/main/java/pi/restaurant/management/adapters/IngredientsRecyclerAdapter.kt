package pi.restaurant.management.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.R
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.Ingredient
import pi.restaurant.management.databinding.ItemIngredientsBinding
import pi.restaurant.management.utils.StringFormatUtils
import java.util.*
import kotlin.collections.ArrayList


class IngredientsRecyclerAdapter(
    private val dataSet: MutableList<Ingredient>,
    private val fragment: Fragment,
) :
    AbstractRecyclerAdapter<IngredientsRecyclerAdapter.ViewHolder>() {
    override val allDataSet: MutableList<Ingredient> = ArrayList(dataSet)

    class ViewHolder(
        val binding: ItemIngredientsBinding,
        val context: Context,
        val fragment: Fragment,
        private val dataSet: List<Ingredient>
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                openItemPreview()
            }
        }

        private fun openItemPreview() {
            val bundle = Bundle()
            bundle.putString("id", dataSet[layoutPosition].id)

            fragment.findNavController()
                .navigate(R.id.actionIngredientsToPreviewIngredient, bundle)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemIngredientsBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment, dataSet)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewName.text = dataSet[position].name
        viewHolder.binding.textViewAmountWithUnit.text =
            StringFormatUtils.formatAmountWithUnit(fragment.context!!, dataSet[position].amount, dataSet[position].unit)
        viewHolder.binding.textViewIngredientType.text = if (dataSet[position].subDish) "Sub-Dish" else "Ingredient"
    }

    override fun getItemCount() = dataSet.size

    override fun clearDataSet() {
        dataSet.clear()
    }

    override fun resetDataSet() {
        dataSet.addAll(allDataSet)
    }

    override fun addItemToDataSet(item: AbstractDataObject, text: String) {
        val ingredient = item as Ingredient
        if (ingredient.name.lowercase(Locale.getDefault()).contains(text)) {
            dataSet.add(ingredient)
        }
    }
}

