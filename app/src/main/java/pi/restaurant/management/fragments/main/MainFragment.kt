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
import pi.restaurant.management.activities.WorkersActivity
import pi.restaurant.management.databinding.FragmentMainBinding
import pi.restaurant.management.utils.Utils

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

        setCardViews()
    }

    private fun setCardViews() {
        Utils.setCardView(
            binding.cardRestaurantData, R.drawable.restaurant_data, R.string.restaurant_data,
            findNavController(), R.id.actionMainToRestaurantData
        )

        Utils.setCardView(
            binding.cardWorkers, R.drawable.worker, R.string.workers,
            findNavController(), R.id.actionMainToWorkers
        )

        Utils.setCardView(
            binding.cardDishes, R.drawable.dish, R.string.dishes,
            findNavController(), R.id.actionMainToDishes
        )

        Utils.setCardView(
            binding.cardAllergens, R.drawable.allergen, R.string.allergens,
            findNavController(), R.id.actionMainToAllergens
        )

        Utils.setCardView(
            binding.cardOrders, R.drawable.order, R.string.orders,
            findNavController(), R.id.actionMainToOrders
        )

        Utils.setCardView(
            binding.cardIngredients, R.drawable.ingredient, R.string.ingredients,
            findNavController(), R.id.actionMainToIngredients
        )

        Utils.setCardView(
            binding.cardDiscounts, R.drawable.discount, R.string.discounts,
            findNavController(), R.id.actionMainToDiscounts
        )

        Utils.setCardView(
            binding.cardSettings, R.drawable.settings, R.string.action_settings,
            findNavController(), R.id.actionMainToSettings
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}