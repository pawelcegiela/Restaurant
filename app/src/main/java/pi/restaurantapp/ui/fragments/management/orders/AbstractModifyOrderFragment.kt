package pi.restaurantapp.ui.fragments.management.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import pi.restaurantapp.BR
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentModifyOrderBinding
import pi.restaurantapp.viewmodels.activities.management.OrdersViewModel
import pi.restaurantapp.viewmodels.fragments.management.orders.AbstractModifyOrderViewModel
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails
import pi.restaurantapp.objects.enums.CollectionType
import pi.restaurantapp.objects.enums.Precondition
import pi.restaurantapp.ui.fragments.AbstractModifyItemFragment
import pi.restaurantapp.ui.pickers.CustomNumberPicker
import pi.restaurantapp.logic.utils.ComputingUtils
import pi.restaurantapp.logic.utils.PreconditionUtils
import java.util.*


abstract class AbstractModifyOrderFragment : AbstractModifyItemFragment() {

    private var _binding: FragmentModifyOrderBinding? = null
    val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation get() = binding.toolbarNavigation
    override var itemId = ""
    abstract val editDishActionId: Int
    abstract val addDishAction: Int

    protected open val activityViewModel: OrdersViewModel by activityViewModels()
    private val _viewModel get() = viewModel as AbstractModifyOrderViewModel

    private lateinit var numberPickerCollectionTime: CustomNumberPicker


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _viewModel.setDeliveryOptions(activityViewModel.deliveryOptions.value)
        if (activityViewModel.savedOrder.value == null) {
            super.onViewCreated(view, savedInstanceState)
        } else {
            viewModel.itemId = activityViewModel.savedOrder.value?.id ?: ""
            _viewModel.setItem(activityViewModel.savedOrder.value ?: Order())
            _viewModel.setPreviousStatus(activityViewModel.previousStatus.value ?: -1)
            addLiveDataObservers()
            viewModel.getUserRole()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyOrderBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.fragment = this
        binding.lifecycleOwner = this
        linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun initializeUI() {
        numberPickerCollectionTime =
            CustomNumberPicker(binding.numberPickerCollectionTime, 30, 150, 5) { refreshCollectionDate() }
    }

    override fun fillInData() {
        val data = _viewModel.item.value ?: Order()

        if (_viewModel.previousStatus.value == null) {
            _viewModel.setPreviousStatus(activityViewModel.previousStatus.value ?: -1)
        }

        numberPickerCollectionTime =
            CustomNumberPicker(binding.numberPickerCollectionTime, 30, 150, 5) { refreshCollectionDate() }
        numberPickerCollectionTime.setValue(ComputingUtils.getMinutesFromDate(data.details.orderDate, data.basic.collectionDate))

        if (this is AddOrderFragment) {
            setNavigationCardsSave()
        } else {
            setNavigationCardsSaveRemove()
        }
        finishLoading()
    }

    private fun refreshCollectionDate() {
        _viewModel.item.value?.basic?.collectionDate =
            ComputingUtils.getDateTimeXMinutesAfterDate(_viewModel.item.value?.details?.orderDate ?: Date(), numberPickerCollectionTime.getValue())
        // TODO Checking whether it's not too late
    }

    fun editDish(dishItem: DishItem) {
        val sdo = getDataObject()
        activityViewModel.setSavedOrder(Order(sdo.id, sdo.basic as OrderBasic, sdo.details as OrderDetails))
        activityViewModel.setEditedDish(dishItem)
        findNavController().navigate(editDishActionId)
    }

    fun onItemSelectedCollectionType(position: Int) {
        _viewModel.item.value?.basic?.collectionType = position
        if (position == CollectionType.DELIVERY.ordinal) {
            binding.linearLayoutDeliveryDetails.visibility = View.VISIBLE
            binding.spinnerPlace.setSelection(CollectionType.SELF_PICKUP.ordinal)
        } else {
            binding.linearLayoutDeliveryDetails.visibility = View.GONE
        }
        binding.spinnerPlace.isEnabled = position == CollectionType.SELF_PICKUP.ordinal
        _viewModel.observer.notifyPropertyChanged(BR.dishesList)
    }

    fun onClickButtonAddDish() {
        val sdo = getDataObject()
        activityViewModel.setSavedOrder(Order(sdo.id, sdo.basic as OrderBasic, sdo.details as OrderDetails))
        findNavController().navigate(addDishAction)
    }

    override fun getDataObject(): SplitDataObject {
        if (activityViewModel.previousStatus.value == null) {
            activityViewModel.setPreviousStatus(_viewModel.item.value?.basic?.orderStatus)
        }
        if (activityViewModel.deliveryOptions.value == null) {
            activityViewModel.setDeliveryOptions(_viewModel.observer.deliveryOptions)
        }

        return SplitDataObject(_viewModel.item.value!!.id, _viewModel.item.value!!.basic, _viewModel.item.value!!.details)
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        if (_viewModel.isDeliveryOrder()) {
            map[binding.address.editTextStreet] = R.string.street
            map[binding.address.editTextHouseNumber] = R.string.house_number
            map[binding.address.editTextPostalCode] = R.string.postal_code
            map[binding.address.editTextCity] = R.string.city
        }
        return map
    }

    open fun removeDish(dishItem: DishItem) {
        val itemPosition = _viewModel.observer.dishesList.indexOf(dishItem)
        _viewModel.observer.dishesList.remove(dishItem)
        binding.recyclerViewDishes.adapter?.notifyItemRemoved(itemPosition)
        _viewModel.updateFullPrice()
    }

    override fun checkSavePreconditions(data: SplitDataObject): Precondition {
        if (super.checkSavePreconditions(data) != Precondition.OK) {
            return super.checkSavePreconditions(data)
        }
        return PreconditionUtils.checkOrder(data.basic as OrderBasic, data.details as OrderDetails, _viewModel.observer.deliveryOptions)
    }
}