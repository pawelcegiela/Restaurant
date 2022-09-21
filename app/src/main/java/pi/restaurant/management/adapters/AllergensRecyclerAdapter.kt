package pi.restaurant.management.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.R
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.Allergen
import pi.restaurant.management.databinding.ItemAllergensBinding
import java.util.*


class AllergensRecyclerAdapter(
    private val dataSet: MutableList<Allergen>,
    private val fragment: Fragment,
) :
    AbstractRecyclerAdapter<AllergensRecyclerAdapter.ViewHolder>() {
    override val allDataSet: MutableList<Allergen> = ArrayList(dataSet)

    class ViewHolder(
        val binding: ItemAllergensBinding,
        val context: Context,
        val fragment: Fragment,
        private val dataSet: List<Allergen>
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
                .navigate(R.id.actionAllergensToPreviewAllergen, bundle)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAllergensBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment, dataSet)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewName.text = dataSet[position].name
    }

    override fun getItemCount() = dataSet.size

    override fun clearDataSet() {
        dataSet.clear()
    }

    override fun resetDataSet() {
        dataSet.addAll(allDataSet)
    }

    override fun addItemToDataSet(item: AbstractDataObject, text: String) {
        val allergen = item as Allergen
        if (allergen.name.lowercase(Locale.getDefault()).contains(text)) {
            dataSet.add(allergen)
        }
    }

}

