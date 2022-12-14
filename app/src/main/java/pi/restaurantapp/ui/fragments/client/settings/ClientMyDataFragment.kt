package pi.restaurantapp.ui.fragments.client.settings

import android.content.Context
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
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.enums.CollectionType
import pi.restaurantapp.ui.fragments.AbstractModifyItemFragment
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.viewmodels.fragments.client.settings.ClientMyDataViewModel

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for ClientMyDataFragment.
 * @see pi.restaurantapp.viewmodels.fragments.client.settings.ClientMyDataViewModel ViewModel layer
 * @see pi.restaurantapp.logic.fragments.client.settings.ClientMyDataLogic Model layer
 */
class ClientMyDataFragment : AbstractModifyItemFragment() {

    override var nextActionId = R.id.actionClientMyDataToSettings
    override val saveMessageId = R.string.your_data_has_been_changed
    override val removeMessageId = 0 // Warning: unused
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation get() = binding.toolbarNavigation
    override var itemId = ""

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
        binding.vm = _viewModel
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        itemId = Firebase.auth.uid!!
        return binding.root
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextFirstName] = R.string.first_name
        map[binding.editTextLastName] = R.string.last_name
        map[binding.editTextEmail] = R.string.e_mail
        return map
    }

    override fun initializeUI() {}

    fun onItemSelectedCollectionType(position: Int) {
        if (position == CollectionType.DELIVERY.ordinal) {
            binding.spinnerOrderPlace.setSelection(CollectionType.SELF_PICKUP.ordinal)
        }
        binding.spinnerOrderPlace.isEnabled = position == CollectionType.SELF_PICKUP.ordinal
    }

    override fun afterSave() {
        val sharedPref = activity?.getSharedPreferences("prefs", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("name", StringFormatUtils.format(binding.editTextFirstName.text.toString(), binding.editTextLastName.text.toString()))
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