package pi.restaurant.management.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.R
import pi.restaurant.management.databinding.ItemPreviewDishIngredientBinding
import pi.restaurant.management.objects.data.ingredient.IngredientItem
import pi.restaurant.management.objects.enums.IngredientStatus
import pi.restaurant.management.utils.StringFormatUtils


class PreviewDishIngredientRecyclerAdapter(
    private val dataSet: List<Pair<IngredientItem, IngredientStatus>>,
    private val fragment: Fragment
) :
    RecyclerView.Adapter<PreviewDishIngredientRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: ItemPreviewDishIngredientBinding,
        val context: Context,
        val fragment: Fragment
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPreviewDishIngredientBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (dataSet[position].second == IngredientStatus.OTHER) {
            viewHolder.binding.imageIcon.setImageResource(R.drawable.optional)
        } else if (dataSet[position].second == IngredientStatus.POSSIBLE) {
            viewHolder.binding.imageIcon.setImageResource(R.drawable.plus)
        }
        viewHolder.binding.textViewName.text =
            StringFormatUtils.formatIngredientItemHeader(dataSet[position].first, dataSet[position].second, viewHolder.context)
    }

    override fun getItemCount() = dataSet.size

}

