package pi.restaurant.management.ui.fragments.management.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import pi.restaurant.management.databinding.FragmentPasswordBinding
import pi.restaurant.management.model.fragments.management.settings.PasswordViewModel

class PasswordFragment : Fragment() {
    private var _binding: FragmentPasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        setButtonListener()
        setViewModelObservers()
    }

    private fun setButtonListener() {
        binding.toolbarNavigation.root.visibility = View.VISIBLE
        binding.toolbarNavigation.cardSave.root.visibility = View.VISIBLE
        binding.toolbarNavigation.cardSave.root.setOnClickListener {
            viewModel.changePassword(
                binding.editTextCurrentPassword.text.toString(),
                binding.editTextNewPassword.text.toString(),
                binding.editTextRepeatNewPassword.text.toString()
            )
        }
    }

    private fun setViewModelObservers() {
        // TODO Wyjście z fragmentu po sukcesie, nakaz wypełnienia wszystkich pól
        viewModel.liveCurrentPassword.observe(viewLifecycleOwner) { value ->
            binding.editTextCurrentPassword.setText(value)
        }

        viewModel.liveNewPassword.observe(viewLifecycleOwner) { value ->
            binding.editTextNewPassword.setText(value)
        }

        viewModel.liveRepeatNewPassword.observe(viewLifecycleOwner) { value ->
            binding.editTextRepeatNewPassword.setText(value)
        }

        viewModel.messageId.observe(viewLifecycleOwner) { messageId ->
            Toast.makeText(activity, getString(messageId), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}