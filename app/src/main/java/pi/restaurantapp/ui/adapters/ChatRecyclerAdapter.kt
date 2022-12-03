package pi.restaurantapp.ui.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pi.restaurantapp.R
import pi.restaurantapp.databinding.ItemChatBinding
import pi.restaurantapp.objects.data.chat.Message
import pi.restaurantapp.logic.utils.StringFormatUtils

class ChatRecyclerAdapter(
    private val dataSet: MutableList<Message>,
    private val fragment: Fragment,
    private val mainUserId: String,
    private val mainUserDisplay: Boolean
) :
    RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: ItemChatBinding,
        val context: Context,
        val fragment: Fragment
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChatBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val message = dataSet[position]

        viewHolder.binding.textViewMessage.text = message.message
        viewHolder.binding.textViewDate.text = StringFormatUtils.formatDateTime(message.timestamp)

        viewHolder.binding.textViewUser.text = message.authorName
        if ((mainUserDisplay && message.authorId == mainUserId) || (!mainUserDisplay && message.authorId != mainUserId)) {
            viewHolder.binding.cardViewMessage.backgroundTintList = ColorStateList.valueOf(fragment.resources.getColor(R.color.blue_2))
            val layoutParams = TableRow.LayoutParams(
                viewHolder.binding.textViewMessage.layoutParams.width,
                viewHolder.binding.textViewMessage.layoutParams.height
            )
            layoutParams.gravity = Gravity.END
            layoutParams.setMargins(40, 15, 40, 15)
            viewHolder.binding.cardViewMessage.layoutParams = layoutParams
        }
        if (mainUserDisplay && message.authorId == mainUserId) {
            viewHolder.binding.textViewUser.visibility = View.GONE
        }
    }

    override fun getItemCount() = dataSet.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position
}