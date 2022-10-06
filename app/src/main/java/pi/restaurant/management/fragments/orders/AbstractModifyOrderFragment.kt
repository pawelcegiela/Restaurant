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
import pi.restaurant.management.adapters.ModifyOrderDishesRecyclerAdapter
import pi.restaurant.management.data.*
import pi.restaurant.management.databinding.FragmentModifyOrderBinding
import pi.restaurant.management.enums.DeliveryType
import pi.restaurant.management.enums.OrderPlace
import pi.restaurant.management.enums.OrderStatus
import pi.restaurant.management.enums.OrderType
import pi.restaurant.management.fragments.AbstractModifyItemFragment
import pi.restaurant.management.utils.StringFormatUtils
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
    override val cardSetNavigation get() = binding.cardSetNavigation
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
        initializeSpinners()
        initializeButton()
    }

    private fun initializeSpinners() {
        binding.spinnerType.adapter =
            ArrayAdapter(
                requireContext(),
                R.layout.spinner_item_view,
                R.id.itemTextView,
                OrderType.getArrayOfStrings(requireContext())
            )

        binding.spinnerStatus.adapter =
            ArrayAdapter(
                requireContext(),
                R.layout.spinner_item_view,
                R.id.itemTextView,
                OrderStatus.getArrayOfStrings(requireContext())
            )

        binding.spinnerDelivery.adapter =
            ArrayAdapter(
                requireContext(),
                R.layout.spinner_item_view,
                R.id.itemTextView,
                DeliveryType.getArrayOfStrings(requireContext())
            )

        binding.spinnerPlace.adapter =
            ArrayAdapter(
                requireContext(),
                R.layout.spinner_item_view,
                R.id.itemTextView,
                OrderPlace.getArrayOfStrings(requireContext())
            )
    }

    fun initializeRecycler() {
        binding.recyclerViewDishes.adapter = ModifyOrderDishesRecyclerAdapter(dishesList, this)
        SubItemUtils.setRecyclerSize(binding.recyclerViewDishes, dishesList.size, requireContext())
    }

    private fun initializeButton() {
        binding.buttonAddDish.setOnClickListener {
            findNavController().navigate(addDishAction)
        }
    }

    override fun getDataObject(): SplitDataObject {
        itemId = itemId.ifEmpty { StringFormatUtils.formatId() }

        val basic = OrderBasic(
            id = itemId,
            orderStatus = binding.spinnerStatus.selectedItemId.toInt(),
            collectionDate = Utils.getTodayWithTime(binding.editTextCollectionTime.text.toString()),
            deliveryType = binding.spinnerDelivery.selectedItemId.toInt(),
            value = dishesList.sumOf { it.finalPrice },
            name = "Order"
        )
        val details = OrderDetails(
            id = itemId,
            userId = Firebase.auth.uid ?: "",
            orderType = binding.spinnerType.selectedItemId.toInt(),
            orderDate = orderDate ?: Date(),
            orderPlace = binding.spinnerPlace.selectedItemId.toInt(),
            dishes = HashMap(dishesList.associateBy { it.id }),
            address = getAddress()
        )

        return SplitDataObject(itemId, basic, details)
    }

    private fun getAddress(): AddressBasic? {
        return if (binding.spinnerDelivery.selectedItemId.toInt() == DeliveryType.DELIVERY.ordinal) {
            AddressBasic(
                id = itemId,
                city = binding.address.editTextCity.text.toString(),
                postalCode = binding.address.editTextPostalCode.text.toString(),
                street = binding.address.editTextStreet.text.toString(),
                houseNumber = binding.address.editTextHouseNumber.text.toString(),
                flatNumber = binding.address.editTextFlatNumber.text.toString()
            )
        } else {
            null
        }
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextCollectionTime] = R.string.collection_time
        return map
    }

    fun removeDish(dishItem: DishItem) {
        dishesList.remove(dishItem)
        binding.recyclerViewDishes.adapter?.notifyDataSetChanged() //TODO Zła praktyka
        SubItemUtils.setRecyclerSize(binding.recyclerViewDishes, dishesList.size, requireContext())
    }

    fun addLiveDataListener() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<DishItem>("newItem")
            ?.observe(viewLifecycleOwner) {
                dishesList.add(it)
                SubItemUtils.setRecyclerSize(binding.recyclerViewDishes, dishesList.size, requireContext())
                Toast.makeText(context, R.string.dish_added, Toast.LENGTH_SHORT).show()
            }
    }
}