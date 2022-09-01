package pi.restaurant.management.adapters

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.R
import pi.restaurant.management.data.User
import pi.restaurant.management.databinding.ItemWorkersBinding
import pi.restaurant.management.enums.Role


class WorkersRecyclerAdapter(
    private val dataSet: List<User>,
    private val fragment: Fragment,
) :
    RecyclerView.Adapter<WorkersRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: ItemWorkersBinding,
        val context: Context,
        val fragment: Fragment,
        private val dataSet: List<User>
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                openItemPreview()
            }
        }

        private fun openItemPreview() {
            val bundle = Bundle()
            bundle.putString("id", dataSet[layoutPosition].id)

            fragment.findNavController().navigate(R.id.actionWorkersToPreviewWorker, bundle)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWorkersBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment, dataSet)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewName.text =
            dataSet[position].firstName + " " + dataSet[position].lastName

        if (dataSet[position].disabled) {
            viewHolder.binding.textViewName.paintFlags =
                viewHolder.binding.textViewName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        viewHolder.binding.textViewRole.text =
            viewHolder.context.getString(Role.getNameResById(dataSet[position].role))
    }

    override fun getItemCount() = dataSet.size

}

