package pi.restaurantapp.ui.fragments.client.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentClientMyDataBinding
import pi.restaurantapp.model.fragments.management.AbstractModifyItemViewModel
import pi.restaurantapp.model.fragments.management.workers.AbstractModifyWorkerViewModel
import pi.restaurantapp.model.fragments.management.workers.EditWorkerViewModel
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.address.AddressBasic
import pi.restaurantapp.objects.data.user.User
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.data.user.UserDetails
import pi.restaurantapp.objects.enums.Role
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

    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : EditWorkerViewModel by viewModels()
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
    }

    override fun getDataObject(): SplitDataObject {
        val user = (viewModel as AbstractModifyWorkerViewModel).getPreviousItem()
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
            ordersToDeliver = user.details.ordersToDeliver,
            defaultDeliveryAddress = getAddress(),
            contactPhone = binding.editTextContactPhone.text.toString()
        )

        return SplitDataObject(itemId, basic, details)
    }

    private fun getAddress() : AddressBasic {
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

        setNavigationCardsSave()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}