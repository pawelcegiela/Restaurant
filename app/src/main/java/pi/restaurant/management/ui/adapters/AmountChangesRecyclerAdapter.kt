package pi.restaurant.management.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.databinding.ItemAmountChangeBinding
import pi.restaurant.management.objects.data.ingredient.IngredientAmountChange
import pi.restaurant.management.objects.enums.IngredientModificationType
import pi.restaurant.management.utils.StringFormatUtils


class AmountChangesRecyclerAdapter(
    private val dataSet: List<IngredientAmountChange>,
    private val fragment: Fragment,
    private val unit: String
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
        val valueAfter = dataSet[position].valueAfter.toString() + " " + unit
        val changedValue = (if (dataSet[position].changedValue > 0) "+" else "") + dataSet[position].changedValue + " " + unit

        viewHolder.binding.textViewDateType.text = "${StringFormatUtils.formatDateTime(dataSet[position].date)} - " +
                IngredientModificationType.getString(dataSet[position].modificationType, viewHolder.context)
        viewHolder.binding.textViewValues.text = "$valueAfter  ($changedValue)"
    }

    override fun getItemCount() = dataSet.size

}

