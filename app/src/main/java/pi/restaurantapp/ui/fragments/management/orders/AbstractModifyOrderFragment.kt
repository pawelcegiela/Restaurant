package pi.restaurantapp.ui.fragments.management.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentModifyOrderBinding
import pi.restaurantapp.model.activities.management.OrdersViewModel
import pi.restaurantapp.model.fragments.management.orders.AbstractModifyOrderViewModel
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.address.AddressBasic
import pi.restaurantapp.objects.data.delivery.DeliveryBasic
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails
import pi.restaurantapp.objects.enums.*
import pi.restaurantapp.ui.RecyclerManager
import pi.restaurantapp.ui.adapters.OrderDishesRecyclerAdapter
import pi.restaurantapp.ui.fragments.AbstractModifyItemFragment
import pi.restaurantapp.ui.pickers.CustomNumberPicker
import pi.restaurantapp.ui.adapters.SpinnerAdapter
import pi.restaurantapp.utils.ComputingUtils
import pi.restaurantapp.utils.PreconditionUtils
import pi.restaurantapp.utils.StringFormatUtils
import java.math.BigDecimal
import java.util.*


abstract class AbstractModifyOrderFragment : AbstractModifyItemFragment() {

    private var _binding: FragmentModifyOrderBinding? = null
    val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation get() = binding.toolbarNavigation
    override var itemId = ""
    abstract val editDishActionId: Int

    protected open val activityViewModel: OrdersViewModel by activityViewModels()

    protected var dishesList: MutableList<DishItem> = ArrayList()
    abstract val addDishAction: Int
    private lateinit var numberPickerCollectionTime: CustomNumberPicker

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (activityViewModel.savedOrder.value == null) {
            super.onViewCreated(view, savedInstanceState)
        } else {
            (viewModel as AbstractModifyOrderViewModel).setItem(activityViewModel.savedOrder.value ?: Order())
            (viewModel as AbstractModifyOrderViewModel).setPreviousStatus(activityViewModel.previousStatus.value ?: -1)
            (viewModel as AbstractModifyOrderViewModel).setDeliveryOptions(activityViewModel.deliveryOptions.value ?: DeliveryBasic())
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
        if (activityViewModel.deliveryOptions.value == null) {
            activityViewModel.setDeliveryOptions((viewModel as AbstractModifyOrderViewModel).deliveryOptions.value ?: DeliveryBasic())
        }
        if ((viewModel as AbstractModifyOrderViewModel).previousStatus.value == null) {
            (viewModel as AbstractModifyOrderViewModel).setPreviousStatus(activityViewModel.previousStatus.value ?: -1)
        }

        binding.editTextName.setText(if (data.basic.name == getString(R.string.order)) "" else data.basic.name)
        binding.spinnerType.setSelection(data.details.orderType)
        numberPickerCollectionTime.setValue(ComputingUtils.getMinutesFromDate(data.details.orderDate, data.basic.collectionDate))
        if (((viewModel as AbstractModifyOrderViewModel).deliveryOptions.value ?: DeliveryBasic()).available) {
            binding.spinnerCollectionType.setSelection(data.basic.collectionType)
        } else {
            binding.spinnerCollectionType.isEnabled = false
            binding.spinnerCollectionType.setSelection(CollectionType.SELF_PICKUP.ordinal)
        }
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

        if (this is AddOrderFragment) {
            setNavigationCardsSave()
        } else {
            setNavigationCardsSaveRemove()
        }
        finishLoading()
    }

    private fun refreshCollectionDate() {
        // TODO Checking whether it's not too late
    }

    fun editDish(dishItem: DishItem) {
        val sdo = getDataObject()
        activityViewModel.setSavedOrder(Order(sdo.id, sdo.basic as OrderBasic, sdo.details as OrderDetails))
        activityViewModel.setEditedDish(dishItem)
        findNavController().navigate(editDishActionId)
    }

    private fun initializeSpinners() {
        val context = requireContext()
        binding.spinnerCollectionType.adapter = SpinnerAdapter(context, CollectionType.getArrayOfStrings(context))
        binding.spinnerType.adapter = SpinnerAdapter(context, OrderType.getArrayOfStrings(context))
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
                updateFullPrice()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }
    }

    fun initializeRecycler() {
        binding.recyclerViewDishes.adapter = OrderDishesRecyclerAdapter(dishesList, this)
        binding.recyclerViewDishes.layoutManager = RecyclerManager(context)
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
            orderStatus = order.basic.orderStatus,
            collectionDate = ComputingUtils.getDateTimeXMinutesAfterDate(order.details.orderDate, numberPickerCollectionTime.getValue()),
            collectionType = binding.spinnerCollectionType.selectedItemId.toInt(),
            value = countFullPrice(),
            name = if (binding.editTextName.text?.isEmpty() == true) "Order" else binding.editTextName.text.toString(),
            userId = Firebase.auth.uid ?: ""
        )
        val details = OrderDetails(
            id = itemId,
            orderType = binding.spinnerType.selectedItemId.toInt(),
            orderDate = order.details.orderDate,
            modificationDate = Date(),
            orderPlace = binding.spinnerPlace.selectedItemId.toInt(),
            dishes = HashMap(dishesList.associateBy { it.id }),
            address = getAddress(),
            statusChanges = order.details.statusChanges,
            delivererId = order.details.delivererId,
            contactPhone = binding.editTextContactPhone.text.toString(),
            comments = binding.editTextComments.text.toString()
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

    open fun removeDish(dishItem: DishItem) {
        val itemPosition = dishesList.indexOf(dishItem)
        dishesList.remove(dishItem)
        binding.recyclerViewDishes.adapter?.notifyItemRemoved(itemPosition)
        updateFullPrice()
    }

    private fun countFullPrice(): String {
        var price = dishesList.sumOf { BigDecimal(it.finalPrice) }
        if (binding.spinnerCollectionType.selectedItemId.toInt() == CollectionType.DELIVERY.ordinal) {
            val deliveryOptions = (viewModel as AbstractModifyOrderViewModel).deliveryOptions.value ?: return price.toString()
            if (price < BigDecimal(deliveryOptions.minimumPriceFreeDelivery)) {
                price += BigDecimal(deliveryOptions.extraDeliveryFee)
            }
        }
        return price.toString()
    }

    override fun checkSavePreconditions(data: SplitDataObject): Precondition {
        if (super.checkSavePreconditions(data) != Precondition.OK) {
            return super.checkSavePreconditions(data)
        }
        return PreconditionUtils.checkOrder(data.basic as OrderBasic, data.details as OrderDetails, (viewModel as AbstractModifyOrderViewModel).deliveryOptions.value)
    }

    private fun updateFullPrice() {
        binding.textViewFullPrice.text = StringFormatUtils.formatPrice(countFullPrice())
    }

    private fun isDeliveryOrder(): Boolean {
        return binding.spinnerCollectionType.selectedItemId.toInt() == CollectionType.DELIVERY.ordinal
    }
}