package pi.restaurantapp.ui.fragments.client.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentClientMainBinding
import pi.restaurantapp.utils.UserInterfaceUtils

class ClientMainFragment : Fragment() {
    private var _binding: FragmentClientMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCardViews()
    }

    private fun setCardViews() {
        UserInterfaceUtils.setCardView(
            binding.cardRestaurantData, R.drawable.restaurant_data, R.string.restaurant_data,
            findNavController(), R.id.actionClientMainToRestaurantData
        )

        UserInterfaceUtils.setCardView(
            binding.cardNewOrder, R.drawable.plus, R.string.new_order,
            findNavController(), R.id.actionClientMainToNewOrder
        )

        UserInterfaceUtils.setCardView(
            binding.cardOrdersHistory, R.drawable.order, R.string.orders_history,
            findNavController(), R.id.actionClientMainToOrders
        )

        UserInterfaceUtils.setCardView(
            binding.cardChats, R.drawable.chat, R.string.chat,
            findNavController(), R.id.actionClientMainToChats
        )

        UserInterfaceUtils.setCardView(
            binding.cardPreferences, R.drawable.settings, R.string.my_account_and_preferences,
            findNavController(), R.id.actionClientMainToSettings
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}