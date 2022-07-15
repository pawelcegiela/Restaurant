package pi.restaurant.management.fragments.restaurantdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurant.management.R
import pi.restaurant.management.databinding.FragmentRdMainBinding

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

        binding.buttonAboutRestaurant.setOnClickListener {
            findNavController().navigate(R.id.actionRDtoAboutRestaurant)
        }

        binding.buttonOpeningHours.setOnClickListener {
            findNavController().navigate(R.id.actionRDtoOpeningHours)
        }

        binding.buttonLocation.setOnClickListener {
            findNavController().navigate(R.id.actionRDtoLocation)
        }

        binding.buttonDelivery.setOnClickListener {
            findNavController().navigate(R.id.actionRDtoDelivery)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}