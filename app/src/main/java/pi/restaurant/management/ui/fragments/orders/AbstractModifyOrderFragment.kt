package pi.restaurant.management.ui.fragments.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.databinding.FragmentModifyOrderBinding
import pi.restaurant.management.model.activities.OrdersViewModel
import pi.restaurant.management.model.fragments.orders.AbstractModifyOrderViewModel
import pi.restaurant.management.objects.data.*
import pi.restaurant.management.objects.data.address.AddressBasic
import pi.restaurant.management.objects.data.dish.DishItem
import pi.restaurant.management.objects.data.order.Order
import pi.restaurant.management.objects.data.order.OrderBasic
import pi.restaurant.management.objects.data.order.OrderDetails
import pi.restaurant.management.objects.enums.CollectionType
import pi.restaurant.management.objects.enums.OrderPlace
import pi.restaurant.management.objects.enums.OrderStatus
import pi.restaurant.management.objects.enums.OrderType
import pi.restaurant.management.ui.adapters.OrderDishesRecyclerAdapter
import pi.restaurant.management.ui.fragments.AbstractModifyItemFragment
import pi.restaurant.management.ui.views.CustomNumberPicker
import pi.restaurant.management.ui.views.SpinnerAdapter
import pi.restaurant.management.utils.ComputingUtils
import pi.restaurant.management.utils.StringFormatUtils
import pi.restaurant.management.utils.UserInterfaceUtils
import java.util.*


abstract class AbstractModifyOrderFragment : AbstractModifyItemFragment() {

    private var _binding: FragmentModifyOrderBinding? = null
    val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val cardSetNavigation get() = binding.cardSetNavigation
    override var itemId = ""

    protected val activityViewModel: OrdersViewModel by activityViewModels()

    var dishesList: MutableList<DishItem> = ArrayList()
    abstract val addDishAction: Int
    private lateinit var numberPickerCollectionTime: CustomNumberPicker

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (activityViewModel.savedOrder.value == null) {
            super.onViewCreated(view, savedInstanceState)
        } else {
            (viewModel as AbstractModifyOrderViewModel).setItem(activityViewModel.savedOrder.value ?: Order())
            (viewModel as AbstractModifyOrderViewModel).setPreviousStatus(activityViewModel.previousStatus.value ?: -1)
            itemId = activityViewModel.savedOrder.value?.id ?: ""
            addLiveDataObservers()
            viewModel.getUserRole()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyOrderBinding.inflate(inflater, container, false)
        linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun initializeUI() {
        numberPickerCollectionTime =
            CustomNumberPicker(binding.numberPickerCollectionTime, 30, 150, 5) { refreshCollectionDate() }
        numberPickerCollectionTime.setValue(45)
        initializeSpinners()
        initializeButton()
    }

    override fun fillInData() {
        val data = (viewModel as AbstractModifyOrderViewModel).item.value ?: Order()

        if (activityViewModel.previousStatus.value == null) {
            activityViewModel.setPreviousStatus((viewModel as AbstractModifyOrderViewModel).item.value?.basic?.orderStatus)
        }
        if ((viewModel as AbstractModifyOrderViewModel).previousStatus.value == null) {
            (viewModel as AbstractModifyOrderViewModel).setPreviousStatus(activityViewModel.previousStatus.value ?: -1)
        }

        binding.editTextName.setText(if (data.basic.name == getString(R.string.order)) "" else data.basic.name)
        binding.spinnerType.setSelection(data.details.orderType)
        setSpinnerStatusSelection(data.basic.orderStatus)
        numberPickerCollectionTime.setValue(ComputingUtils.getMinutesFromDate(data.details.orderDate, data.basic.collectionDate))
        binding.spinnerCollectionType.setSelection(data.basic.collectionType)
        binding.spinnerPlace.setSelection(data.details.orderPlace)
        if (data.basic.collectionType == CollectionType.SELF_PICKUP.ordinal) {
            binding.linearLayoutDeliveryDetails.visibility = View.GONE
        } else {
            binding.address.editTextCity.setText(data.details.address?.city)
            binding.address.editTextPostalCode.setText(data.details.address?.postalCode)
            binding.address.editTextStreet.setText(data.details.address?.street)
            binding.address.editTextHouseNumber.setText(data.details.address?.houseNumber)
            binding.address.editTextFlatNumber.setText(data.details.address?.flatNumber)
        }
        binding.textViewOrderDate.text = StringFormatUtils.formatDateTime(data.details.orderDate)
        binding.editTextContactPhone.setText(data.details.contactPhone)
        dishesList = data.details.dishes.toList().map { it.second }.toMutableList()
        updateFullPrice()
        initializeRecycler()

        finishLoading()
    }

    private fun setSpinnerStatusSelection(status: Int) {
        val statuses = if (isDeliveryOrder()) OrderStatus.getStatusesForDelivery() else OrderStatus.getStatusesForSelfPickup()
        binding.spinnerStatus.setSelection(statuses.indexOf(status))
    }

    private fun refreshCollectionDate() {
        // TODO Sprawdzanie czy nie jest za późno
    }

    private fun initializeSpinners() {
        val context = requireContext()
        binding.spinnerCollectionType.adapter = SpinnerAdapter(context, CollectionType.getArrayOfStrings(context))
        binding.spinnerType.adapter = SpinnerAdapter(context, OrderType.getArrayOfStrings(context))
        binding.spinnerStatus.adapter = SpinnerAdapter(context, getArrayOfStatuses())
        binding.spinnerPlace.adapter = SpinnerAdapter(context, OrderPlace.getArrayOfStrings(context))

        binding.spinnerCollectionType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                if (position == CollectionType.DELIVERY.ordinal) {
                    binding.linearLayoutDeliveryDetails.visibility = View.VISIBLE
                    binding.spinnerPlace.setSelection(CollectionType.SELF_PICKUP.ordinal)
                } else {
                    binding.linearLayoutDeliveryDetails.visibility = View.GONE
                }
                binding.spinnerPlace.isEnabled = position == CollectionType.SELF_PICKUP.ordinal
                binding.spinnerStatus.adapter = SpinnerAdapter(context, getArrayOfStatuses())
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }
    }

    private fun getArrayOfStatuses(): Array<String> {
        return if (isDeliveryOrder()) {
            OrderStatus.getArrayOfStringsForDelivery(requireContext())
        } else {
            OrderStatus.getArrayOfStringsForSelfPickup(requireContext())
        }
    }

    fun initializeRecycler() {
        binding.recyclerViewDishes.adapter = OrderDishesRecyclerAdapter(dishesList, this)
    }

    private fun initializeButton() {
        binding.buttonAddDish.setOnClickListener {
            val sdo = getDataObject()
            activityViewModel.setSavedOrder(Order(sdo.id, sdo.basic as OrderBasic, sdo.details as OrderDetails))
            findNavController().navigate(addDishAction)
        }
    }

    override fun getDataObject(): SplitDataObject {
        val order = (viewModel as AbstractModifyOrderViewModel).getPreviousItem()
        itemId = itemId.ifEmpty { StringFormatUtils.formatId() }

        val basic = OrderBasic(
            id = itemId,
            orderStatus = getStatusInt(),
            collectionDate = ComputingUtils.getDateTimeXMinutesAfterDate(order.details.orderDate, numberPickerCollectionTime.getValue()),
            collectionType = binding.spinnerCollectionType.selectedItemId.toInt(),
            value = countFullPrice(),
            name = if (binding.editTextName.text?.isEmpty() == true) "Order" else binding.editTextName.text.toString()
        )
        val details = OrderDetails(
            id = itemId,
            userId = Firebase.auth.uid ?: "",
            orderType = binding.spinnerType.selectedItemId.toInt(),
            orderDate = order.details.orderDate,
            modificationDate = Date(),
            orderPlace = binding.spinnerPlace.selectedItemId.toInt(),
            dishes = HashMap(dishesList.associateBy { it.id }),
            address = getAddress(),
            statusChanges = order.details.statusChanges,
            delivererId = order.details.delivererId,
            contactPhone = binding.editTextContactPhone.text.toString()
        )

        return SplitDataObject(itemId, basic, details)
    }

    private fun getAddress(): AddressBasic? {
        return if (isDeliveryOrder()) {
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

    private fun getStatusInt() : Int {
        val statuses = if (isDeliveryOrder()) {
            OrderStatus.getStatusesForDelivery()
        } else {
            OrderStatus.getStatusesForSelfPickup()
        }
        return statuses[binding.spinnerStatus.selectedItemId.toInt()]
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        if (isDeliveryOrder()) {
            map[binding.address.editTextStreet] = R.string.street
            map[binding.address.editTextHouseNumber] = R.string.house_number
            map[binding.address.editTextPostalCode] = R.string.postal_code
            map[binding.address.editTextCity] = R.string.city
        }
        return map
    }

    fun removeDish(dishItem: DishItem) {
        dishesList.remove(dishItem)
        binding.recyclerViewDishes.adapter?.notifyDataSetChanged() //TODO Zła praktyka
        updateFullPrice()
    }

    private fun countFullPrice(): Double {
        return dishesList.sumOf { it.finalPrice }
    }

    private fun updateFullPrice() {
        binding.textViewFullPrice.text = StringFormatUtils.formatPrice(countFullPrice())
    }

    private fun isDeliveryOrder(): Boolean {
        return binding.spinnerCollectionType.selectedItemId.toInt() == CollectionType.DELIVERY.ordinal
    }
}