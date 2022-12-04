package pi.restaurantapp.ui.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import pi.restaurantapp.R
import pi.restaurantapp.databinding.ItemIngredientsBinding
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.ingredient.IngredientBasic
import java.util.*


class IngredientsRecyclerAdapter(
    private val dataSet: MutableList<IngredientBasic>,
    private val fragment: Fragment,
) :
    AbstractRecyclerAdapter<IngredientsRecyclerAdapter.ViewHolder>() {
    override val allDataSet: MutableList<IngredientBasic> = ArrayList(dataSet)

    class ViewHolder(
        val binding: ItemIngredientsBinding,
        val context: Context,
        val fragment: Fragment,
        private val dataSet: List<IngredientBasic>
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
        if (dataSet[position].subDish) {
            viewHolder.binding.textViewAmount.visibility = View.GONE
        } else {
            viewHolder.binding.textViewAmount.text =
                StringFormatUtils.formatAmountWithUnit(fragment.requireContext(), dataSet[position].amount.toString(), dataSet[position].unit)
        }
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
        val ingredient = item as IngredientBasic
        if (ingredient.name.lowercase(Locale.getDefault()).contains(text)) {
            dataSet.add(ingredient)
        }
    }
}

