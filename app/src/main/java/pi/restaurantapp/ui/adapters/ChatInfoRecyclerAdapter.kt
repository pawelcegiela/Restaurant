package pi.restaurantapp.ui.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import pi.restaurantapp.R
import pi.restaurantapp.databinding.ItemChatInfoBinding
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.chat.ChatInfo
import java.util.*

/**
 * Custom adapter for recycler view with basic chat information.
 */
class ChatInfoRecyclerAdapter(
    private val dataSet: MutableList<ChatInfo>,
    private val fragment: Fragment
) :
    AbstractRecyclerAdapter<ChatInfoRecyclerAdapter.ViewHolder>() {
    override val allDataSet: MutableList<ChatInfo> = ArrayList(dataSet)

    class ViewHolder(
        val binding: ItemChatInfoBinding,
        val context: Context,
        val fragment: Fragment,
        private val dataSet: List<ChatInfo>
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                openItemPreview()
            }
        }

        private fun openItemPreview() {
            val bundle = Bundle()
            bundle.putString("id", dataSet[layoutPosition].id)

            fragment.findNavController().navigate(R.id.actionChooseCustomerToChatWithCustomer, bundle)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChatInfoBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment, dataSet)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewName.text = dataSet[position].authorName
    }

    override fun getItemCount() = dataSet.size

    override fun clearDataSet() {
        dataSet.clear()
    }

    override fun resetDataSet() {
        dataSet.addAll(allDataSet)
    }

    override fun addItemToDataSet(item: AbstractDataObject, text: String) {
        val chatInfo = item as ChatInfo
        if (chatInfo.authorName.lowercase(Locale.getDefault()).contains(text)) {
            dataSet.add(chatInfo)
        }
    }

}

