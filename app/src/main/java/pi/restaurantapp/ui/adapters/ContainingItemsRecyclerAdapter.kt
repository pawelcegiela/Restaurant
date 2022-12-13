package pi.restaurantapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pi.restaurantapp.databinding.ItemContainingBinding

/**
 * Custom adapter for recycler view with items containing an allergen or ingredient.
 */
class ContainingItemsRecyclerAdapter(
    private val dataSet: List<String>,
    private val fragment: Fragment
) :
    RecyclerView.Adapter<ContainingItemsRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: ItemContainingBinding,
        val context: Context,
        val fragment: Fragment
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContainingBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewName.text = dataSet[position]
    }

    override fun getItemCount() = dataSet.size

    companion object {
        @JvmStatic
        fun createNew(dataSet: List<String>, fragment: Fragment): ContainingItemsRecyclerAdapter {
            return ContainingItemsRecyclerAdapter(dataSet, fragment)
        }
    }
}

