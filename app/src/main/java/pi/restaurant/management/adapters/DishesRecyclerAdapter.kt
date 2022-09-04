package pi.restaurant.management.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.R
import pi.restaurant.management.data.Dish
import pi.restaurant.management.databinding.ItemDishesBinding


class DishesRecyclerAdapter(
    private val dataSet: List<Dish>,
    private val fragment: Fragment,
) :
    RecyclerView.Adapter<DishesRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: ItemDishesBinding,
        val context: Context,
        val fragment: Fragment,
        private val dataSet: List<Dish>
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
                .navigate(R.id.actionDishesToPreviewDish, bundle)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDishesBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment, dataSet)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewName.text = dataSet[position].name
        viewHolder.binding.textViewPrice.text = "${dataSet[position].basePrice} zł" //TODO
    }

    override fun getItemCount() = dataSet.size

}

