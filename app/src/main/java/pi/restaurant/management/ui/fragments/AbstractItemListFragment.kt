package pi.restaurant.management.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurant.management.R
import pi.restaurant.management.ui.adapters.AbstractRecyclerAdapter
import pi.restaurant.management.databinding.FragmentItemListBinding
import pi.restaurant.management.objects.enums.Precondition
import pi.restaurant.management.objects.enums.Role
import pi.restaurant.management.model.fragments.AbstractItemListViewModel

abstract class AbstractItemListFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null
    protected val binding get() = _binding!!

    abstract val viewModel: AbstractItemListViewModel

    abstract val addActionId: Int
    abstract val editActionId: Int
    val progressBar get() = binding.progress.progressBar
    private lateinit var chosenItemId: String

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

    open fun initializeUI() {
        initializeSearchView()

        binding.fab.setOnClickListener {
            findNavController().navigate(addActionId)
        }
    }

    private fun initializeSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                (binding.recyclerView.adapter as AbstractRecyclerAdapter?)?.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                (binding.recyclerView.adapter as AbstractRecyclerAdapter?)?.filter(newText)
                return false
            }
        })
    }

    private fun openEdit() {
        val bundle = Bundle()
        bundle.putString("id", chosenItemId)

        findNavController().navigate(editActionId, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}