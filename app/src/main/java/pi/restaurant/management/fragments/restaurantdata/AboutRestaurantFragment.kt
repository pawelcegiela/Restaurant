package pi.restaurant.management.fragments.restaurantdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.AboutRestaurant
import pi.restaurant.management.databinding.FragmentAboutRestaurantBinding
import pi.restaurant.management.fragments.AbstractModifyItemFragment
import pi.restaurant.management.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.fragments.orders.EditOrderViewModel

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

    override fun fillInData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<AboutRestaurant>() ?: return
        binding.editTextRestaurantName.setText(data.name)
        binding.editTextRestaurantDescription.setText(data.description)
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextRestaurantName] = R.string.restaurant_name
        map[binding.editTextRestaurantDescription] = R.string.restaurant_description
        return map
    }

    override fun getDataObject(): AboutRestaurant {
        val data = AboutRestaurant()

        data.name = binding.editTextRestaurantName.text.toString()
        data.description = binding.editTextRestaurantDescription.text.toString()

        return data
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}