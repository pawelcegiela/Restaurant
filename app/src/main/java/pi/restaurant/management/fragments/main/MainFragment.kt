package pi.restaurant.management.fragments.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurant.management.R
import pi.restaurant.management.activities.RestaurantDataActivity
import pi.restaurant.management.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButtonListeners()
    }

    private fun setButtonListeners() {
        binding.buttonRestaurantData.setOnClickListener {
            startActivity(Intent(activity, RestaurantDataActivity::class.java))
        }

        binding.buttonWorkers.setOnClickListener {
            findNavController().navigate(R.id.actionMainToWorkers)
        }

        binding.buttonMenu.setOnClickListener {
            findNavController().navigate(R.id.actionMainToMenu)
        }

        binding.buttonOrders.setOnClickListener {
            findNavController().navigate(R.id.actionMainToOrders)
        }

        binding.buttonIngredients.setOnClickListener {
            findNavController().navigate(R.id.actionMainToIngredients)
        }

        binding.buttonDiscounts.setOnClickListener {
            findNavController().navigate(R.id.actionMainToDiscounts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}