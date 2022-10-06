package pi.restaurant.management.ui.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.R
import pi.restaurant.management.objects.data.AbstractDataObject
import pi.restaurant.management.objects.data.order.OrderBasic
import pi.restaurant.management.databinding.ItemOrdersBinding
import pi.restaurant.management.objects.enums.OrderStatus
import pi.restaurant.management.utils.StringFormatUtils
import java.util.*


class OrdersRecyclerAdapter(
    private val dataSet: MutableList<OrderBasic>,
    private val fragment: Fragment,
) :
    AbstractRecyclerAdapter<OrdersRecyclerAdapter.ViewHolder>() {
    override val allDataSet: MutableList<OrderBasic> = ArrayList(dataSet)

    class ViewHolder(
        val binding: ItemOrdersBinding,
        val context: Context,
        val fragment: Fragment,
        private val dataSet: List<OrderBasic>
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
                .navigate(R.id.actionOrdersToPreviewOrder, bundle)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrdersBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment, dataSet)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewCollectionDate.text = StringFormatUtils.formatDate(dataSet[position].collectionDate)
        viewHolder.binding.textViewStatus.text = OrderStatus.getString(dataSet[position].orderStatus, viewHolder.context)
        viewHolder.binding.textViewValue.text = StringFormatUtils.formatPrice(dataSet[position].value)
    }

    override fun getItemCount() = dataSet.size

    override fun clearDataSet() {
        dataSet.clear()
    }

    override fun resetDataSet() {
        dataSet.addAll(allDataSet)
    }

    override fun addItemToDataSet(item: AbstractDataObject, text: String) {
        val order = item as OrderBasic
        if (order.name.lowercase(Locale.getDefault()).contains(text)) {
            dataSet.add(order)
        }
    }

}

