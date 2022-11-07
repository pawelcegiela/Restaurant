package pi.restaurantapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurantapp.databinding.FragmentItemListBinding
import pi.restaurantapp.model.fragments.management.AbstractItemListViewModel
import pi.restaurantapp.objects.enums.Role

abstract class AbstractItemListFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null
    protected val binding get() = _binding!!

    abstract val viewModel: AbstractItemListViewModel

    abstract val addActionId: Int
    val progressBar get() = binding.progress.progressBar
    lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        searchView = binding.searchView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserRole()
        viewModel.loadData()
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
                    binding.fabAdd.visibility = View.VISIBLE
                }
                if (viewModel.displayFAB()) {
                    binding.fabFilter.visibility = View.VISIBLE
                }
            }
        }
    }

    abstract fun addViewPagerAdapters()

    open fun initializeUI() {
        addViewPagerAdapters()

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(addActionId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}