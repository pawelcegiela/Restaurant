package pi.restaurant.management.ui.fragments.restaurantdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.objects.data.*
import pi.restaurant.management.objects.data.aboutrestaurant.AboutRestaurant
import pi.restaurant.management.objects.data.aboutrestaurant.AboutRestaurantBasic
import pi.restaurant.management.objects.data.aboutrestaurant.AboutRestaurantDetails
import pi.restaurant.management.databinding.FragmentAboutRestaurantBinding
import pi.restaurant.management.ui.fragments.AbstractModifyItemFragment
import pi.restaurant.management.model.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.model.fragments.restaurantdata.AboutRestaurantViewModel
import pi.restaurant.management.objects.SnapshotsPair
import pi.restaurant.management.utils.StringFormatUtils

class AboutRestaurantFragment : AbstractModifyItemFragment() {
    private var _binding: FragmentAboutRestaurantBinding? = null
    private val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val cardSetNavigation get() = binding.cardSetNavigation
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
        setNavigationCardsSave()
    }

    override fun fillInData() {
        val data = _viewModel.item.value ?: AboutRestaurant()
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