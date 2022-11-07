package pi.restaurantapp.ui.fragments.client.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentClientMyDataBinding
import pi.restaurantapp.model.fragments.client.settings.ClientMyDataViewModel
import pi.restaurantapp.model.fragments.management.AbstractModifyItemViewModel
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.address.AddressBasic
import pi.restaurantapp.objects.data.user.User
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.data.user.UserDetails
import pi.restaurantapp.objects.enums.CollectionType
import pi.restaurantapp.objects.enums.OrderPlace
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.ui.adapters.SpinnerAdapter
import pi.restaurantapp.ui.fragments.AbstractModifyItemFragment
import pi.restaurantapp.utils.StringFormatUtils

class ClientMyDataFragment : AbstractModifyItemFragment() {

    override var nextActionId = R.id.actionClientMyDataToSettings
    override val saveMessageId = R.string.your_data_has_been_changed
    override val removeMessageId = 0 // Warning: unused
    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation get() = binding.toolbarNavigation
    override var itemId = ""
    override val lowestRole = Role.CUSTOMER.ordinal

    override val viewModel: AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel: ClientMyDataViewModel by viewModels()
    private var _binding: FragmentClientMyDataBinding? = null
    val binding get() = _binding!!
    var disabled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientMyDataBinding.inflate(inflater, container, false)
        linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextFirstName] = R.string.first_name
        map[binding.editTextLastName] = R.string.last_name
        map[binding.editTextEmail] = R.string.e_mail
        return map
    }

    override fun initializeUI() {
        itemId = Firebase.auth.uid ?: return

        initializeSpinners()
    }

    override fun getDataObject(): SplitDataObject {
        val user = _viewModel.getPreviousItem()
        itemId = itemId.ifEmpty { StringFormatUtils.formatId() }

        val basic = UserBasic(
            id = itemId,
            firstName = binding.editTextFirstName.text.toString(),
            lastName = binding.editTextLastName.text.toString(),
            role = Role.CUSTOMER.ordinal,
            disabled = user.basic.disabled,
            delivery = false
        )
        val details = UserDetails(
            id = itemId,
            email = binding.editTextEmail.text.toString(),
            creationDate = user.details.creationDate,
            defaultDeliveryAddress = getAddress(),
            contactPhone = binding.editTextContactPhone.text.toString(),
            preferredCollectionType = binding.spinnerCollectionType.selectedItemId.toInt(),
            preferredOrderPlace = binding.spinnerOrderPlace.selectedItemId.toInt()
        )

        return SplitDataObject(itemId, basic, details)
    }

    private fun getAddress(): AddressBasic {
        return AddressBasic(
            city = binding.defaultDeliveryAddress.editTextCity.text.toString(),
            postalCode = binding.defaultDeliveryAddress.editTextPostalCode.text.toString(),
            street = binding.defaultDeliveryAddress.editTextStreet.text.toString(),
            houseNumber = binding.defaultDeliveryAddress.editTextHouseNumber.text.toString(),
            flatNumber = binding.defaultDeliveryAddress.editTextFlatNumber.text.toString()
        )
    }

    override fun fillInData() {
        val data = _viewModel.item.value ?: User()
        binding.editTextFirstName.setText(data.basic.firstName)
        binding.editTextLastName.setText(data.basic.lastName)
        binding.editTextEmail.setText(data.details.email)
        binding.defaultDeliveryAddress.editTextCity.setText(data.details.defaultDeliveryAddress.city)
        binding.defaultDeliveryAddress.editTextPostalCode.setText(data.details.defaultDeliveryAddress.postalCode)
        binding.defaultDeliveryAddress.editTextStreet.setText(data.details.defaultDeliveryAddress.street)
        binding.defaultDeliveryAddress.editTextHouseNumber.setText(data.details.defaultDeliveryAddress.houseNumber)
        binding.defaultDeliveryAddress.editTextFlatNumber.setText(data.details.defaultDeliveryAddress.flatNumber)
        binding.editTextContactPhone.setText(data.details.contactPhone)
        binding.spinnerCollectionType.setSelection(data.details.preferredCollectionType)
        binding.spinnerOrderPlace.setSelection(data.details.preferredOrderPlace)

        setNavigationCardsSave()
    }

    private fun initializeSpinners() {
        val context = requireContext()
        binding.spinnerCollectionType.adapter = SpinnerAdapter(context, CollectionType.getArrayOfStrings(context))
        binding.spinnerOrderPlace.adapter = SpinnerAdapter(context, OrderPlace.getArrayOfStrings(context))

        binding.spinnerCollectionType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                if (position == CollectionType.DELIVERY.ordinal) {
                    binding.spinnerOrderPlace.setSelection(CollectionType.SELF_PICKUP.ordinal)
                }
                binding.spinnerOrderPlace.isEnabled = position == CollectionType.SELF_PICKUP.ordinal
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }
    }

    override fun afterSave() {
        val sharedPref = activity?.getSharedPreferences("prefs", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("city", binding.defaultDeliveryAddress.editTextCity.text.toString())
            putString("postalCode", binding.defaultDeliveryAddress.editTextPostalCode.text.toString())
            putString("street", binding.defaultDeliveryAddress.editTextStreet.text.toString())
            putString("houseNumber", binding.defaultDeliveryAddress.editTextHouseNumber.text.toString())
            putString("flatNumber", binding.defaultDeliveryAddress.editTextFlatNumber.text.toString())
            putString("contactPhone", binding.editTextContactPhone.text.toString())
            putInt("collectionType", binding.spinnerCollectionType.selectedItemId.toInt())
            putInt("orderPlace", binding.spinnerOrderPlace.selectedItemId.toInt())
            apply()
        }

        super.afterSave()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}