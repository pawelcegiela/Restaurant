package pi.restaurant.management.ui.fragments.restaurantdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurant.management.R
import pi.restaurant.management.databinding.FragmentRdMainBinding
import pi.restaurant.management.utils.UserInterfaceUtils

class RDMainFragment : Fragment() {
    private var _binding: FragmentRdMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRdMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCardViews()
    }

    private fun setCardViews() {
        UserInterfaceUtils.setCardView(
            binding.cardAboutRestaurant, R.drawable.about_restaurant, R.string.about_restaurant,
            findNavController(), R.id.actionRDtoAboutRestaurant
        )

        UserInterfaceUtils.setCardView(
            binding.cardOpeningHours, R.drawable.hours, R.string.opening_hours,
            findNavController(), R.id.actionRDtoOpeningHours
        )

        UserInterfaceUtils.setCardView(
            binding.cardLocation, R.drawable.location, R.string.location,
            findNavController(), R.id.actionRDtoLocation
        )

        UserInterfaceUtils.setCardView(
            binding.cardDelivery, R.drawable.delivery, R.string.delivery,
            findNavController(), R.id.actionRDtoDelivery
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}