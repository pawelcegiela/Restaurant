package pi.restaurantapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pi.restaurantapp.R
import pi.restaurantapp.databinding.ItemSubItemBinding
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.ui.fragments.management.dishes.AbstractModifyDishFragment


class DishAllergensRecyclerAdapter(
    private val dataSet: List<AllergenBasic>,
    private val fragment: Fragment,
) :
    RecyclerView.Adapter<DishAllergensRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: ItemSubItemBinding,
        val context: Context,
        val fragment: Fragment,
        private val dataSet: List<AllergenBasic>,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            initialize()
        }

        private fun initialize() {
            if (fragment is AbstractModifyDishFragment) {
                binding.buttonOptions.text = fragment.getString(R.string.remove)
                binding.buttonOptions.setOnClickListener {
                    fragment.removeAllergenItem(dataSet[layoutPosition])
                }
            } else {
                binding.buttonOptions.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSubItemBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment, dataSet)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewName.text = dataSet[position].name
    }

    override fun getItemCount() = dataSet.size

    companion object {
        @JvmStatic
        fun createNew(dataSet: List<AllergenBasic>, fragment: Fragment): DishAllergensRecyclerAdapter {
            return DishAllergensRecyclerAdapter(dataSet, fragment)
        }
    }
}

