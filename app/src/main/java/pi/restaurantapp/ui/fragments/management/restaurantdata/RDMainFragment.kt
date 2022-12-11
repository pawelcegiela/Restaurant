package pi.restaurantapp.ui.fragments.management.restaurantdata

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.viewModels
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentRdMainBinding
import pi.restaurantapp.databinding.ToolbarNavigationPreviewBinding
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.logic.utils.UserInterfaceUtils
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
    var mapView: MapView? = null

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.initializeData("")
        addLiveDataObservers()

        mapView?.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE, MotionEvent.ACTION_DOWN -> binding.linearLayout.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> binding.linearLayout.requestDisallowInterceptTouchEvent(false)
            }
            mapView!!.onTouchEvent(event)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRdMainBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.fragment = this
        mapView = binding.mapView
        mapView?.getMapboxMap()?.loadStyleUri(Style.MAPBOX_STREETS)
        return binding.root
    }

    override fun initializeExtraData() {
        val item = _viewModel.item.value ?: RestaurantData(RestaurantDataBasic(), RestaurantDataDetails())
        if (item.basic.location.longitude != null && item.basic.location.latitude != null) {
            addAnnotationToMap(Point.fromLngLat(item.basic.location.longitude!!, item.basic.location.latitude!!))
        }

        val openingHours = item.basic.openingHours
        for (i in openingHours.enabledList.indices) {
            if (openingHours.enabledList[i]) {
                dayLayouts[i].visibility = View.VISIBLE
                for (child in dayLayouts[i].children) {
                    if (child is TextViewDetail) {
                        child.text = StringFormatUtils.formatOpeningHours(openingHours.startHoursList[i], openingHours.endHoursList[i])
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

    fun onClickZoom(zoomIn: Boolean) {
        val cameraState = mapView?.getMapboxMap()?.cameraState ?: return
        val cameraOptions = CameraOptions.Builder()
            .center(cameraState.center)
            .zoom(if (zoomIn) cameraState.zoom + 0.5 else cameraState.zoom - 0.5)
            .build()
        mapView?.getMapboxMap()?.setCamera(cameraOptions)
    }

    private fun addAnnotationToMap(point: Point) {
        UserInterfaceUtils.bitmapFromDrawableRes(
            requireActivity(),
            R.drawable.marker
        )?.let {
            val annotationApi = mapView?.annotations
            val pointAnnotationManager = annotationApi?.createPointAnnotationManager()
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(it)
            pointAnnotationManager?.create(pointAnnotationOptions)

            mapView?.getMapboxMap()?.setCamera(
                CameraOptions.Builder()
                    .center(point)
                    .zoom(12.0)
                    .build()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}