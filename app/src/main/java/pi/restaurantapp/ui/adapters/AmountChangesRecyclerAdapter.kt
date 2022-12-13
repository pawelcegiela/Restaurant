package pi.restaurantapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pi.restaurantapp.databinding.ItemAmountChangeBinding
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.ingredient.IngredientAmountChange
import pi.restaurantapp.objects.enums.IngredientModificationType
import pi.restaurantapp.objects.enums.Unit

/**
 * Custom adapter for recycler view with ingredient amount changes.
 */
class AmountChangesRecyclerAdapter(
    private val dataSet: List<IngredientAmountChange>,
    private val fragment: Fragment,
    private val unit: Int
) :
    RecyclerView.Adapter<AmountChangesRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: ItemAmountChangeBinding,
        val context: Context,
        val fragment: Fragment
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAmountChangeBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val valueAfter = dataSet[position].valueAfter.toString() + " " + Unit.getString(unit, fragment.requireContext())
        val changedValue = (if (dataSet[position].changedValue > 0) "+" else "") + dataSet[position].changedValue + " " + Unit.getString(
            unit,
            fragment.requireContext()
        )

        viewHolder.binding.textViewDateType.text = "${StringFormatUtils.formatDateTime(dataSet[position].date)} - " +
                IngredientModificationType.getString(dataSet[position].modificationType, viewHolder.context)
        viewHolder.binding.textViewValues.text = "$valueAfter  ($changedValue)"
    }

    override fun getItemCount() = dataSet.size

    companion object {
        @JvmStatic
        fun createNew(dataSet: List<IngredientAmountChange>, fragment: Fragment, unit: Int): AmountChangesRecyclerAdapter {
            return AmountChangesRecyclerAdapter(dataSet, fragment, unit)
        }
    }
}

