package pi.restaurantapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurantapp.databinding.ToolbarNavigationPreviewBinding
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.objects.enums.Role

abstract class AbstractPreviewItemFragment : Fragment() {
    abstract val viewModel: AbstractPreviewItemViewModel

    abstract val progressBar: ProgressBar
    abstract val toolbarNavigation: ToolbarNavigationPreviewBinding
    abstract val editActionId: Int
    abstract val backActionId: Int
    var editable: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.itemId = arguments?.getString("id").toString()
        viewModel.getUserRole()
        addLiveDataObservers()
    }

    open fun addLiveDataObservers() {
        viewModel.userRole.observe(viewLifecycleOwner) { role ->
            if (role != Role.getPlaceholder()) {
                if (viewModel.shouldGetDataFromDatabase()) {
                    viewModel.getDataFromDatabase()
                } else {
                    viewModel.setReadyToInitialize()
                }
            }
        }

        viewModel.readyToInitialize.observe(viewLifecycleOwner) { ready ->
            if (ready) {
                fillInData()
                if (editable && !viewModel.isDisabled() && Role.isAtLeastManager(viewModel.userRole.value)) {
                    initializeUI()
                } else {
                    initializeWorkerUI()
                }
            }
        }

        viewModel.readyToUnlock.observe(viewLifecycleOwner) { ready ->
            if (ready) {
                progressBar.visibility = View.GONE
            }
        }
    }

    open fun initializeUI() {
        toolbarNavigation.root.visibility = View.VISIBLE
        toolbarNavigation.cardBack.root.visibility = View.GONE
        toolbarNavigation.cardEdit.root.visibility = View.VISIBLE

        toolbarNavigation.cardEdit.root.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", viewModel.itemId)

            findNavController().navigate(editActionId, bundle)
        }
    }

    open fun initializeWorkerUI() {
        toolbarNavigation.root.visibility = View.VISIBLE
        toolbarNavigation.cardBack.root.visibility = View.VISIBLE
        toolbarNavigation.cardEdit.root.visibility = View.GONE

        toolbarNavigation.cardBack.root.setOnClickListener {
            findNavController().navigate(backActionId)
        }
        viewModel.setReadyToUnlock()
    }

    abstract fun fillInData()
}