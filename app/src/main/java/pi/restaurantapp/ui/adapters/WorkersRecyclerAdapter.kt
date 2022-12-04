package pi.restaurantapp.ui.adapters

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import pi.restaurantapp.R
import pi.restaurantapp.databinding.ItemWorkersBinding
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.enums.Role
import java.util.*


class WorkersRecyclerAdapter(
    private val dataSet: MutableList<UserBasic>,
    private val fragment: Fragment,
) :
    AbstractRecyclerAdapter<WorkersRecyclerAdapter.ViewHolder>() {
    override val allDataSet: MutableList<UserBasic> = ArrayList(dataSet)

    class ViewHolder(
        val binding: ItemWorkersBinding,
        val context: Context,
        val fragment: Fragment,
        private val dataSet: List<UserBasic>
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
        viewHolder.binding.textViewName.text = StringFormatUtils.format(dataSet[position].firstName, dataSet[position].lastName)

        if (dataSet[position].disabled) {
            viewHolder.binding.textViewName.paintFlags =
                viewHolder.binding.textViewName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        viewHolder.binding.textViewRole.text = Role.getString(dataSet[position].role, viewHolder.context)
    }

    override fun getItemCount() = dataSet.size

    override fun clearDataSet() {
        dataSet.clear()
    }

    override fun resetDataSet() {
        dataSet.addAll(allDataSet)
    }

    override fun addItemToDataSet(item: AbstractDataObject, text: String) {
        val user = item as UserBasic
        if (user.firstName.lowercase(Locale.getDefault()).contains(text) || user.lastName.lowercase(Locale.getDefault()).contains(text)) {
            dataSet.add(user)
        }
    }
}

