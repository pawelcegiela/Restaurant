package pi.restaurant.management.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurant.management.databinding.FragmentItemListBinding
import pi.restaurant.management.model.fragments.AbstractItemListViewModel
import pi.restaurant.management.objects.enums.Role

abstract class AbstractItemListFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null
    protected val binding get() = _binding!!

    abstract val viewModel: AbstractItemListViewModel

    abstract val addActionId: Int
    abstract val editActionId: Int
    val progressBar get() = binding.progress.progressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadData()
        viewModel.getUserRole()
        addLiveDataObservers()
    }

    open fun addLiveDataObservers() {
        viewModel.dataList.observe(viewLifecycleOwner) {
            initializeUI()
            progressBar.visibility = View.GONE
        }

        viewModel.userRole.observe(viewLifecycleOwner) { role ->
            if (role != Role.getPlaceholder()) {
                if (Role.isAtLeastManager(role) && viewModel.displayFAB()) {
                    binding.fab.visibility = View.VISIBLE
                }
            }
        }
    }

    abstract fun addViewPagerAdapters()

    open fun initializeUI() {
        addViewPagerAdapters()

        binding.fab.setOnClickListener {
            findNavController().navigate(addActionId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}