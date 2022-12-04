package pi.restaurantapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pi.restaurantapp.databinding.ItemStatusChangeBinding
import pi.restaurantapp.logic.utils.StringFormatUtils


class StatusChangesRecyclerAdapter(
    private val dataSet: List<Pair<String, Int>>,
    private val fragment: Fragment,
) :
    RecyclerView.Adapter<StatusChangesRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: ItemStatusChangeBinding,
        val context: Context,
        val fragment: Fragment,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStatusChangeBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewName.text = StringFormatUtils.formatStatusChange(dataSet[position], fragment.requireContext())
    }

    override fun getItemCount() = dataSet.size

    companion object {
        @JvmStatic
        fun createNew(dataSet: List<Pair<String, Int>>, fragment: Fragment): StatusChangesRecyclerAdapter {
            return StatusChangesRecyclerAdapter(dataSet, fragment)
        }
    }
}

