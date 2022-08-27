package pi.restaurant.management.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.R
import pi.restaurant.management.data.DiscountGroup


class DiscountsRecyclerAdapter(
    private val dataSet: List<DiscountGroup>,
    private val fragment: Fragment,
) :
    RecyclerView.Adapter<DiscountsRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        view: View,
        val context: Context,
        val fragment: Fragment,
    ) : RecyclerView.ViewHolder(view) {
        val textViewCode: TextView
        val textViewNumber: TextView
        val textViewDiscountAmount: TextView
        val textViewExpirationDate: TextView

        init {
            textViewCode = view.findViewById(R.id.textViewCode)
            textViewNumber = view.findViewById(R.id.textViewNumber)
            textViewDiscountAmount = view.findViewById(R.id.textViewDiscountAmount)
            textViewExpirationDate = view.findViewById(R.id.textViewExpirationDate)

//            view.setOnClickListener {
//                val bundle = Bundle()
//                bundle.putString("id", dataSet[layoutPosition].id)
//                fragment.findNavController().navigate(R.id.actionDiscountsToEditDiscount, bundle)
//            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_discounts, viewGroup, false)

        return ViewHolder(view, viewGroup.context, fragment)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textViewCode.text = dataSet[position].code
        viewHolder.textViewNumber.text = getNumberOfDiscounts(position)
        viewHolder.textViewDiscountAmount.text = getDiscountAmount(position)
        viewHolder.textViewExpirationDate.text = dataSet[position].expirationDate.toString()
    }

    private fun getNumberOfDiscounts(position: Int): String {
        return dataSet[position].availableDiscounts.size.toString() + " / " +
                dataSet[position].assignedDiscounts.size.toString() + " / " +
                dataSet[position].usedDiscounts.size.toString()
    }

    private fun getDiscountAmount(position: Int): String {
        return dataSet[position].amount.toString() + dataSet[position].type.toString()
    }

    override fun getItemCount() = dataSet.size

}

