package pi.restaurantapp.ui.fragments.management.restaurantdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentRdMainBinding
import pi.restaurantapp.databinding.ToolbarNavigationPreviewBinding
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.restaurantdata.RestaurantData
import pi.restaurantapp.objects.data.restaurantdata.RestaurantDataBasic
import pi.restaurantapp.objects.data.restaurantdata.RestaurantDataDetails
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.ui.fragments.AbstractPreviewItemFragment
import pi.restaurantapp.ui.textviews.TextViewDetail
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.restaurantdata.RDMainViewModel

class RDMainFragment : AbstractPreviewItemFragment() {
    override val toolbarNavigation: ToolbarNavigationPreviewBinding get() = binding.toolbarNavigation
    override var editActionId = R.id.actionRDToEditRestaurantData
    override val backActionId
        get() = if (Role.isWorkerRole(_viewModel.userRole.value ?: Role.getPlaceholder())) {
            R.id.actionRDToMain
        } else {
            R.id.actionRDToClientMain
        }
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
        viewModel.initializeData("")
        addLiveDataObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRdMainBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun initializeExtraData() {
        val item = _viewModel.item.value ?: RestaurantData(RestaurantDataBasic(), RestaurantDataDetails())
        val openingHours = item.basic.openingHours
        val weekDaysEnabled = openingHours.enabledList
        val weekDaysStartHours = openingHours.startHoursList
        val weekDaysEndHours = openingHours.endHoursList
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

        if (item.basic.delivery.available) {
            binding.textViewFreeDelivery.text =
                if (item.basic.delivery.freeDeliveryAvailable) getString(
                    R.string.from_threshold,
                    StringFormatUtils.formatPrice(item.basic.delivery.minimumPriceFreeDelivery)
                ) else getString(R.string.not_available)
        }

        viewModel.setReadyToUnlock()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}