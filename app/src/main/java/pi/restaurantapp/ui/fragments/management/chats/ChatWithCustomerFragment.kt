package pi.restaurantapp.ui.fragments.management.chats

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.databinding.FragmentChatClientManagementBinding
import pi.restaurantapp.objects.data.chat.Message
import pi.restaurantapp.ui.adapters.ChatRecyclerAdapter
import pi.restaurantapp.viewmodels.fragments.management.chats.ChatWithCustomerViewModel

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for ChatWithCustomerFragment.
 * @see pi.restaurantapp.viewmodels.fragments.management.chats.ChatWithCustomerViewModel ViewModel layer
 * @see pi.restaurantapp.logic.fragments.management.chats.ChatWithCustomerLogic Model layer
 */
class ChatWithCustomerFragment : Fragment() {
    private var _binding: FragmentChatClientManagementBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatWithCustomerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatClientManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.customerId = arguments?.getString("id").toString()
        setButtonOnClickListener()
        viewModel.setFirebaseListener()
        viewModel.messagesList.observe(viewLifecycleOwner) { data ->
            binding.recyclerView.adapter = ChatRecyclerAdapter(data, this, viewModel.customerId, mainUserDisplay = false)
            binding.recyclerView.scrollToPosition(data.size - 1)
        }
    }

    private fun setButtonOnClickListener() {
        val name = activity?.getSharedPreferences("prefs", Context.MODE_PRIVATE)?.getString("name", "Unknown name") ?: return
        binding.buttonSendMessage.setOnClickListener {
            if (binding.editTextNewMessage.text.toString().isNotEmpty()) {
                val text = binding.editTextNewMessage.text.toString()
                val authorId = Firebase.auth.uid ?: return@setOnClickListener
                viewModel.addMessage(Message(text, authorId, name))
                binding.editTextNewMessage.setText("")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}