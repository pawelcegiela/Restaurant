package pi.restaurant.management.ui.fragments.restaurantdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import pi.restaurant.management.R
import pi.restaurant.management.databinding.FragmentRdMainBinding
import pi.restaurant.management.databinding.ToolbarNavigationPreviewBinding
import pi.restaurant.management.model.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.model.fragments.restaurantdata.RDMainViewModel
import pi.restaurant.management.objects.data.restaurantdata.RestaurantData
import pi.restaurant.management.objects.data.restaurantdata.RestaurantDataBasic
import pi.restaurant.management.objects.data.restaurantdata.RestaurantDataDetails
import pi.restaurant.management.ui.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.ui.views.TextViewDetail
import pi.restaurant.management.utils.StringFormatUtils
import pi.restaurant.management.utils.UserInterfaceUtils

class RDMainFragment : AbstractPreviewItemFragment() {
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation: ToolbarNavigationPreviewBinding get() = binding.toolbarNavigation
    override var editActionId = R.id.actionRDToEditRestaurantData
    override val backActionId = R.id.actionRDToMain
    override val viewModel: AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel: RDMainViewModel by viewModels()

    private var _binding: FragmentRdMainBinding? = null
    val binding get() = _binding!!

    private val dayLayouts
        get() = arrayListOf(
            binding.layoutMonday,
            binding.layoutTuesday,
            binding.layoutWednesday,
            binding.layoutThursday,
            binding.layoutFriday,
            binding.layoutSaturday,
            binding.layoutSunday
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.itemId = ""
        viewModel.getUserRole()
        addLiveDataObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRdMainBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun fillInData() {
        val item = _viewModel.item.value ?: RestaurantData(RestaurantDataBasic(), RestaurantDataDetails())
        val openingHours = item.basic.openingHours
        val weekDaysEnabled = openingHours.getWeekDaysEnabled()
        val weekDaysStartHours = openingHours.getWeekDaysStartHour()
        val weekDaysEndHours = openingHours.getWeekDaysEndHour()
        for (i in weekDaysEnabled.indices) {
            if (weekDaysEnabled[i]) {
                dayLayouts[i].visibility = View.VISIBLE
                for (child in dayLayouts[i].children) {
                    if (child is TextViewDetail) {
                        child.text = StringFormatUtils.formatOpeningHours(weekDaysStartHours[i], weekDaysEndHours[i])
                    }
                }
            }
        }
        binding.textViewAddress.text = StringFormatUtils.formatAddress(item.basic.location)

        viewModel.setReadyToUnlock()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}