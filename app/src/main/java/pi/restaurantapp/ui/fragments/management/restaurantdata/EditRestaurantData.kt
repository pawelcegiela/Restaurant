package pi.restaurantapp.ui.fragments.management.restaurantdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentEditRestaurantDataBinding
import pi.restaurantapp.utils.UserInterfaceUtils

class EditRestaurantData : Fragment() {
    private var _binding: FragmentEditRestaurantDataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditRestaurantDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCardViews()
    }

    private fun setCardViews() {
        UserInterfaceUtils.setCardView(
            binding.cardAboutRestaurant, R.drawable.about_restaurant, R.string.about_restaurant,
            findNavController(), R.id.actionERDToEditAboutRestaurant
        )

        UserInterfaceUtils.setCardView(
            binding.cardOpeningHours, R.drawable.hours, R.string.opening_hours,
            findNavController(), R.id.actionERDToEditOpeningHours
        )

        UserInterfaceUtils.setCardView(
            binding.cardLocation, R.drawable.location, R.string.location,
            findNavController(), R.id.actionERDToEditLocation
        )

        UserInterfaceUtils.setCardView(
            binding.cardDelivery, R.drawable.delivery, R.string.delivery,
            findNavController(), R.id.actionERDToEditDelivery
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}