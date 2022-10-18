package pi.restaurant.management.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurant.management.R
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
    lateinit var searchView: SearchView
//    var menuProvider: MenuProvider? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        searchView = binding.searchView
//        menuProvider = setMenuProvider()
//        activity?.addMenuProvider(menuProvider!!)
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

    private fun setMenuProvider(): MenuProvider {
        return object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_menu, menu)
                searchView = menu.findItem(R.id.action_search).actionView as SearchView
                searchView.isIconified = false
                searchView.isIconifiedByDefault = false

                menu.findItem(R.id.action_search).setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                    override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                        searchView.setQuery("", true)
                        return true
                    }

                    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                        searchView.setQuery("", true)
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        activity?.removeMenuProvider(menuProvider!!)
        _binding = null
    }
}