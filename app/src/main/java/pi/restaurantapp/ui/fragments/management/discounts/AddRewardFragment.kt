package pi.restaurantapp.ui.fragments.management.discounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentAddRewardBinding
import pi.restaurantapp.viewmodels.fragments.management.discounts.AddRewardViewModel

class AddRewardFragment : Fragment() {

    private var _binding: FragmentAddRewardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddRewardViewModel by viewModels()

    val nextActionId = R.id.actionAddRewardToDiscounts
    val saveMessageId = R.string.rewards_added

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRewardBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.toastMessage.observe(viewLifecycleOwner) { messageId ->
            Toast.makeText(requireContext(), getString(messageId), Toast.LENGTH_SHORT).show()
        }
        binding.toolbarNavigation.cardBack.root.setOnClickListener {
            findNavController().navigate(R.id.actionAddRewardToDiscounts)
        }
    }

}