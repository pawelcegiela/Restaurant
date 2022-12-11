package pi.restaurantapp.ui.fragments.management.restaurantdata

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapLongClickListener
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentModifyLocationBinding
import pi.restaurantapp.logic.utils.UserInterfaceUtils
import pi.restaurantapp.ui.fragments.AbstractModifyItemFragment
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.restaurantdata.EditLocationViewModel

class EditLocationFragment : AbstractModifyItemFragment() {
    private var _binding: FragmentModifyLocationBinding? = null
    private val binding get() = _binding!!

    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation get() = binding.toolbarNavigation
    override var itemId = "location"
    override val nextActionId = R.id.actionLocationToRD
    override val saveMessageId = R.string.location_modified
    override val removeMessageId = 0 // Unused
    var mapView: MapView? = null
    var pointInitialized = false

    override val viewModel: AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel: EditLocationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyLocationBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.linearLayout.visibility = View.INVISIBLE
        mapView = binding.mapView
        mapView?.getMapboxMap()?.loadStyleUri(Style.MAPBOX_STREETS)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView?.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE, MotionEvent.ACTION_DOWN -> binding.linearLayout.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> binding.linearLayout.requestDisallowInterceptTouchEvent(false)
            }
            mapView!!.onTouchEvent(event)
        }

        _viewModel.point.observe(viewLifecycleOwner) { point ->
            point ?: return@observe
            addAnnotationToMap(point)
        }

        binding.mapView.getMapboxMap().addOnMapLongClickListener { newPoint ->
            _viewModel.setPoint(newPoint)
            mapView?.annotations?.cleanup()
            addAnnotationToMap(newPoint)

            true
        }
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

            if (!pointInitialized) {
                mapView?.getMapboxMap()?.setCamera(
                    CameraOptions.Builder()
                        .center(point)
                        .zoom(12.0)
                        .build()
                )
                pointInitialized = true
            }
        }
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.address.editTextStreet] = R.string.street
        map[binding.address.editTextHouseNumber] = R.string.house_number
        map[binding.address.editTextPostalCode] = R.string.postal_code
        map[binding.address.editTextCity] = R.string.city
        return map
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}