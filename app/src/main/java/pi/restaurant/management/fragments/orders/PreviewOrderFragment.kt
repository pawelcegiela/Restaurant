package pi.restaurant.management.fragments.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.adapters.OrderDishesRecyclerAdapter
import pi.restaurant.management.data.Order
import pi.restaurant.management.databinding.FragmentPreviewOrderBinding
import pi.restaurant.management.enums.DeliveryType
import pi.restaurant.management.enums.OrderPlace
import pi.restaurant.management.enums.OrderStatus
import pi.restaurant.management.enums.OrderType
import pi.restaurant.management.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.utils.StringFormatUtils
import pi.restaurant.management.utils.SubItemUtils

class PreviewOrderFragment : AbstractPreviewItemFragment() {
    override val databasePath = "orders"
    override val linearLayout get() = binding.linearLayout
    override val editButton get() = binding.buttonEdit
    override val editActionId = R.id.actionPreviewOrderToEditOrder

    private var _binding: FragmentPreviewOrderBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun fillInData(dataSnapshot: DataSnapshot) {
        val item = dataSnapshot.getValue<Order>() ?: return
        binding.textViewType.text = OrderType.getString(item.orderType, context!!)
        binding.textViewStatus.text = OrderStatus.getString(item.orderStatus, context!!)
        binding.textViewOrderDate.text = StringFormatUtils.formatDateTime(item.orderDate)
        binding.textViewCollectionDate.text = StringFormatUtils.formatDateTime(item.collectionDate)

        val dishesList = item.dishes.toList().map { it.second }.toMutableList()
        binding.recyclerViewDishes.adapter = OrderDishesRecyclerAdapter(dishesList, this)
        SubItemUtils.setRecyclerSize(binding.recyclerViewDishes, dishesList.size, context!!)

        binding.textViewDelivery.text = DeliveryType.getString(item.deliveryType, context!!)
        binding.textViewPlace.text = OrderPlace.getString(item.orderPlace, context!!)

        binding.progress.progressBar.visibility = View.GONE
    }
}