package pi.restaurant.management.fragments.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.adapters.OrderDishesRecyclerAdapter
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.DishItem
import pi.restaurant.management.data.Order
import pi.restaurant.management.databinding.FragmentModifyOrderBinding
import pi.restaurant.management.enums.DeliveryType
import pi.restaurant.management.enums.OrderPlace
import pi.restaurant.management.enums.OrderStatus
import pi.restaurant.management.enums.OrderType
import pi.restaurant.management.fragments.AbstractModifyItemFragment
import pi.restaurant.management.utils.SubItemUtils
import pi.restaurant.management.utils.Utils
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


abstract class AbstractModifyOrderFragment : AbstractModifyItemFragment() {

    private var _binding: FragmentModifyOrderBinding? = null
    val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val saveButton get() = binding.buttonSave
    override val removeButton get() = binding.buttonRemove
    override var itemId = ""

    var dishesList: MutableList<DishItem> = ArrayList()
    var orderDate: Date? = null
    abstract val addDishAction: Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyOrderBinding.inflate(inflater, container, false)
        linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun initializeUI() {
        setSaveButtonListener()
        initializeSpinners()
        initializeButton()
    }

    private fun initializeSpinners() {
        binding.spinnerType.adapter =
            ArrayAdapter(context!!, R.layout.spinner_item_view, R.id.itemTextView, OrderType.getArrayOfStrings(context!!))

        binding.spinnerStatus.adapter =
            ArrayAdapter(context!!, R.layout.spinner_item_view, R.id.itemTextView, OrderStatus.getArrayOfStrings(context!!))

        binding.spinnerDelivery.adapter =
            ArrayAdapter(context!!, R.layout.spinner_item_view, R.id.itemTextView, DeliveryType.getArrayOfStrings(context!!))

        binding.spinnerPlace.adapter =
            ArrayAdapter(context!!, R.layout.spinner_item_view, R.id.itemTextView, OrderPlace.getArrayOfStrings(context!!))
    }

    fun initializeRecycler() {
        binding.recyclerViewDishes.adapter = OrderDishesRecyclerAdapter(dishesList, this)
        SubItemUtils.setRecyclerSize(binding.recyclerViewDishes, dishesList.size, context!!)
    }

    private fun initializeButton() {
        binding.buttonAddDish.setOnClickListener {
            findNavController().navigate(addDishAction)
        }
    }

    override fun getDataObject(): AbstractDataObject {
        return Order(
            id = itemId,
            userId = Firebase.auth.uid ?: "",
            orderType = binding.spinnerType.selectedItemId.toInt(),
            orderStatus = binding.spinnerStatus.selectedItemId.toInt(),
            orderDate = orderDate ?: Date(),
            collectionDate = Utils.getTodayWithTime(binding.editTextCollectionTime.text.toString()),
            deliveryType = binding.spinnerDelivery.selectedItemId.toInt(),
            orderPlace = binding.spinnerPlace.selectedItemId.toInt(),
            dishes = HashMap(dishesList.associateBy { it.id }),
            value = dishesList.sumOf { it.finalPrice },
            name = "Order"
        )
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextCollectionTime] = R.string.collection_time
        return map
    }

    fun removeDish(dishItem: DishItem) {
        dishesList.remove(dishItem)
        binding.recyclerViewDishes.adapter?.notifyDataSetChanged() //TODO Zła praktyka
        SubItemUtils.setRecyclerSize(binding.recyclerViewDishes, dishesList.size, context!!)
    }

    fun addLiveDataListener() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<DishItem>("newItem")
            ?.observe(viewLifecycleOwner) {
                dishesList.add(it)
                SubItemUtils.setRecyclerSize(binding.recyclerViewDishes, dishesList.size, context!!)
                Toast.makeText(context, R.string.dish_added, Toast.LENGTH_SHORT).show()
            }
    }
}