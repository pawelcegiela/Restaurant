package pi.restaurantapp.ui.fragments.management.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentSettingsMainBinding
import pi.restaurantapp.utils.UserInterfaceUtils

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
        setCardViews()
    }

    private fun setCardViews() {
        UserInterfaceUtils.setCardView(
            binding.cardMyData, R.drawable.my_data, R.string.my_data,
            findNavController(), R.id.actionSettingsToMyData
        )

        UserInterfaceUtils.setCardView(
            binding.cardPassword, R.drawable.password, R.string.password,
            findNavController(), R.id.actionSettingsToPassword
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}