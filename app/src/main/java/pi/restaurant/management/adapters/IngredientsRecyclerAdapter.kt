package pi.restaurant.management.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.R
import pi.restaurant.management.data.Ingredient
import pi.restaurant.management.enums.Unit


class IngredientsRecyclerAdapter(
    private val dataSet: List<Ingredient>,
    private val fragment: Fragment,
) :
    RecyclerView.Adapter<IngredientsRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        view: View,
        val context: Context,
        val fragment: Fragment,
        private val dataSet: List<Ingredient>
    ) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView
        val textViewAmount: TextView

        init {
            textViewName = view.findViewById(R.id.textViewName)
            textViewAmount = view.findViewById(R.id.textViewAmountWithUnit)

            view.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("id", dataSet[layoutPosition].id)

                fragment.findNavController().navigate(R.id.actionIngredientsToEditIngredient, bundle)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_ingredients, viewGroup, false)

        return ViewHolder(view, viewGroup.context, fragment, dataSet)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textViewName.text = dataSet[position].name
        viewHolder.textViewAmount.text = "${dataSet[position].amount} " +
                viewHolder.context.getString(Unit.getNameResById(dataSet[position].unit))
    }

    override fun getItemCount() = dataSet.size

}

