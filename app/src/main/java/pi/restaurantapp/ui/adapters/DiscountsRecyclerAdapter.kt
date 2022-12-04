package pi.restaurantapp.ui.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import pi.restaurantapp.R
import pi.restaurantapp.databinding.ItemDiscountsBinding
import pi.restaurantapp.logic.utils.ComputingUtils
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.ui.activities.management.DiscountsActivity
import java.util.*


class DiscountsRecyclerAdapter(
    private val dataSet: MutableList<DiscountBasic>,
    private val fragment: Fragment,
) :
    AbstractRecyclerAdapter<DiscountsRecyclerAdapter.ViewHolder>() {
    override val allDataSet: MutableList<DiscountBasic> = ArrayList(dataSet)

    class ViewHolder(
        val binding: ItemDiscountsBinding,
        val context: Context,
        val fragment: Fragment,
        private val dataSet: List<DiscountBasic>
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            if (fragment.activity is DiscountsActivity) {
                binding.root.setOnClickListener {
                    openItemPreview()
                }
            }
        }

        private fun openItemPreview() {
            val bundle = Bundle()
            bundle.putString("id", dataSet[layoutPosition].id)

            fragment.findNavController().navigate(R.id.actionDiscountsToPreviewDiscount, bundle)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDiscountsBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment, dataSet)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewCode.text = dataSet[position].id
        viewHolder.binding.textViewNumber.text = ComputingUtils.getNumberOfDiscounts(dataSet[position])
        viewHolder.binding.textViewDiscountAmount.text =
            StringFormatUtils.formatDiscountValue(dataSet[position].amount, dataSet[position].valueType, viewHolder.context)
        viewHolder.binding.textViewExpirationDate.text = StringFormatUtils.formatDate(dataSet[position].expirationDate)
    }

    override fun getItemCount() = dataSet.size

    override fun clearDataSet() {
        dataSet.clear()
    }

    override fun resetDataSet() {
        dataSet.addAll(allDataSet)
    }

    override fun addItemToDataSet(item: AbstractDataObject, text: String) {
        val discount = item as DiscountBasic
        if (discount.id.lowercase(Locale.getDefault()).contains(text)) {
            dataSet.add(discount)
        }
    }

}

