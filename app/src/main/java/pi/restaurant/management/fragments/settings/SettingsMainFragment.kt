package pi.restaurant.management.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurant.management.R
import pi.restaurant.management.databinding.FragmentSettingsMainBinding

class SettingsMainFragment : Fragment() {
    private var _binding: FragmentSettingsMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonMyData.setOnClickListener {
            findNavController().navigate(R.id.actionSettingsToMyData)
        }

        binding.buttonPassword.setOnClickListener {
            findNavController().navigate(R.id.actionSettingsToPassword)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}