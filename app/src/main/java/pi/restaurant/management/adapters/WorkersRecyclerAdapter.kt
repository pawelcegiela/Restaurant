package pi.restaurant.management.adapters

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.R
import pi.restaurant.management.data.UserData
import pi.restaurant.management.enums.Role


class WorkersRecyclerAdapter(
    private val dataSet: List<UserData>,
    private val fragment: Fragment,
    private val userId: String,
    private val userRole: Int
) :
    RecyclerView.Adapter<WorkersRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        view: View,
        val context: Context,
        val fragment: Fragment,
        private val dataSet: List<UserData>,
        private val userId: String,
        private val userRole: Int
    ) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView
        val textViewRole: TextView

        init {
            textViewName = view.findViewById(R.id.textViewName)
            textViewRole = view.findViewById(R.id.textViewRole)

            view.setOnClickListener {
                if (dataSet[layoutPosition].role > userRole) {
                    val bundle = Bundle()
                    bundle.putString("id", dataSet[layoutPosition].id)
                    bundle.putInt("myRole", userRole)

                    fragment.findNavController().navigate(R.id.actionWorkersToEditWorker, bundle)
                } else if (dataSet[layoutPosition].id == userId) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.to_manage_go_to_settings),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.no_permission_user_data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_workers, viewGroup, false)

        return ViewHolder(view, viewGroup.context, fragment, dataSet, userId, userRole)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textViewName.text =
            dataSet[position].firstName + " " + dataSet[position].lastName

        if (dataSet[position].disabled) {
            viewHolder.textViewName.paintFlags =
                viewHolder.textViewName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        viewHolder.textViewRole.text =
            viewHolder.context.getString(Role.getNameResById(dataSet[position].role))
    }

    override fun getItemCount() = dataSet.size

}

