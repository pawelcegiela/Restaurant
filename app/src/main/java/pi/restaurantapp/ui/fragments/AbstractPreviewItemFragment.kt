package pi.restaurantapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurantapp.databinding.ToolbarNavigationPreviewBinding
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.objects.enums.ToolbarType
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel

abstract class AbstractPreviewItemFragment : Fragment() {
    abstract val viewModel: AbstractPreviewItemViewModel

    abstract val toolbarNavigation: ToolbarNavigationPreviewBinding
    abstract val editActionId: Int
    abstract val backActionId: Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initializeData(arguments?.getString("id").toString())
        addLiveDataObservers()
    }

    open fun addLiveDataObservers() {
        viewModel.readyToInitialize.observe(viewLifecycleOwner) { ready ->
            if (ready) {
                initializeExtraData()
                viewModel.setToolbarType()
                initializeNavigationToolbar()
            }
        }
    }

    open fun initializeExtraData() {}

    open fun initializeNavigationToolbar() {
        toolbarNavigation.cardEdit.root.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", viewModel.itemId)

            findNavController().navigate(editActionId, bundle)
        }

        toolbarNavigation.cardBack.root.setOnClickListener {
            findNavController().navigate(backActionId)
        }
    }
}