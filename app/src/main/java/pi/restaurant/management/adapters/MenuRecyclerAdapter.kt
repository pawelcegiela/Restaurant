package pi.restaurant.management.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.R
import pi.restaurant.management.data.Dish


class MenuRecyclerAdapter(
    private val dataSet: List<Dish>,
    private val fragment: Fragment,
) :
    RecyclerView.Adapter<MenuRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        view: View,
        val context: Context,
        val fragment: Fragment,
    ) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView
        val textViewPrice: TextView

        init {
            textViewName = view.findViewById(R.id.textViewName)
            textViewPrice = view.findViewById(R.id.textViewPrice)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_menu, viewGroup, false)

        return ViewHolder(view, viewGroup.context, fragment)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textViewName.text = dataSet[position].name
        viewHolder.textViewPrice.text = "${dataSet[position].price} zł"
    }

    override fun getItemCount() = dataSet.size

}

