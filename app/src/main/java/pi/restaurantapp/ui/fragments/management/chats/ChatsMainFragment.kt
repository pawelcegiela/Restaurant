package pi.restaurantapp.ui.fragments.management.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentChatsMainBinding
import pi.restaurantapp.logic.utils.UserInterfaceUtils

class ChatsMainFragment : Fragment() {
    private var _binding: FragmentChatsMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatsMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCardViews()
    }

    private fun setCardViews() {
        UserInterfaceUtils.setCardView(
            binding.cardRestaurantData, R.drawable.customer, R.string.chats_with_customers,
            findNavController(), R.id.actionChatsToChooseCustomer
        )

        UserInterfaceUtils.setCardView(
            binding.cardNewOrder, R.drawable.forum, R.string.worker_forum,
            findNavController(), R.id.actionChatsToForum
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}