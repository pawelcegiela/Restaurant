package pi.restaurantapp.ui.fragments.management.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentMainBinding
import pi.restaurantapp.utils.UserInterfaceUtils

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
        UserInterfaceUtils.setCardView(
            binding.cardOrders, R.drawable.order, R.string.orders,
            findNavController(), R.id.actionMainToOrders
        )

        UserInterfaceUtils.setCardView(
            binding.cardDishes, R.drawable.dish, R.string.dishes,
            findNavController(), R.id.actionMainToDishes
        )

        UserInterfaceUtils.setCardView(
            binding.cardIngredients, R.drawable.ingredient, R.string.ingredients,
            findNavController(), R.id.actionMainToIngredients
        )

        UserInterfaceUtils.setCardView(
            binding.cardAllergens, R.drawable.allergen, R.string.allergens,
            findNavController(), R.id.actionMainToAllergens
        )

        UserInterfaceUtils.setCardView(
            binding.cardChats, R.drawable.chat, R.string.chats,
            findNavController(), R.id.actionMainToChats
        )

        UserInterfaceUtils.setCardView(
            binding.cardWorkers, R.drawable.worker, R.string.workers,
            findNavController(), R.id.actionMainToWorkers
        )

        UserInterfaceUtils.setCardView(
            binding.cardCustomers, R.drawable.customer, R.string.customers,
            findNavController(), R.id.actionMainToCustomers
        )

        UserInterfaceUtils.setCardView(
            binding.cardDiscounts, R.drawable.discount, R.string.discounts,
            findNavController(), R.id.actionMainToDiscounts
        )

        UserInterfaceUtils.setCardView(
            binding.cardRestaurantData, R.drawable.restaurant_data, R.string.restaurant_data,
            findNavController(), R.id.actionMainToRestaurantData
        )

        UserInterfaceUtils.setCardView(
            binding.cardSettings, R.drawable.settings, R.string.action_settings,
            findNavController(), R.id.actionMainToSettings
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}