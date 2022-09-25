package pi.restaurant.management.fragments.restaurantdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.*
import pi.restaurant.management.databinding.FragmentAboutRestaurantBinding
import pi.restaurant.management.fragments.AbstractModifyItemFragment
import pi.restaurant.management.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.utils.SnapshotsPair
import pi.restaurant.management.utils.StringFormatUtils

class AboutRestaurantFragment : AbstractModifyItemFragment() {
    private var _binding: FragmentAboutRestaurantBinding? = null
    private val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val saveButton get() = binding.buttonSave
    override val removeButton: Button? = null
    override var itemId = "aboutRestaurant"
    override val nextActionId = R.id.actionAboutRestaurantToRD
    override val saveMessageId = R.string.restaurant_data_modified
    override val removeMessageId = 0 // Unused

    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : AboutRestaurantViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutRestaurantBinding.inflate(inflater, container, false)
        binding.linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun initializeUI() {
        setSaveButtonListener()
    }

    private fun getItem(snapshotsPair: SnapshotsPair) : AboutRestaurant {
        val basic = snapshotsPair.basic?.getValue<AboutRestaurantBasic>() ?: AboutRestaurantBasic()
        val details = snapshotsPair.details?.getValue<AboutRestaurantDetails>() ?: AboutRestaurantDetails()
        return AboutRestaurant(basic, details)
    }

    override fun fillInData(snapshotsPair: SnapshotsPair) {
        val data = getItem(snapshotsPair)
        binding.editTextRestaurantName.setText(data.basic.name)
        binding.editTextRestaurantDescription.setText(data.basic.description)
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextRestaurantName] = R.string.restaurant_name
        map[binding.editTextRestaurantDescription] = R.string.restaurant_description
        return map
    }

    override fun getDataObject(): SplitDataObject {
        itemId = itemId.ifEmpty { StringFormatUtils.formatId() }

        val basic = AboutRestaurantBasic(
            name = binding.editTextRestaurantName.text.toString(),
            description = binding.editTextRestaurantDescription.text.toString()
        )

        return SplitDataObject(itemId, basic, AboutRestaurantDetails())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}