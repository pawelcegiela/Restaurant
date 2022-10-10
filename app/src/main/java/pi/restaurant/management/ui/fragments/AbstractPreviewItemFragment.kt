package pi.restaurant.management.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurant.management.databinding.CardSetEditBackBinding
import pi.restaurant.management.objects.enums.Role
import pi.restaurant.management.model.fragments.AbstractPreviewItemViewModel

abstract class AbstractPreviewItemFragment : Fragment() {
    abstract val viewModel: AbstractPreviewItemViewModel

    abstract val progressBar: ProgressBar
    abstract val cardSetNavigation: CardSetEditBackBinding?
    abstract val editActionId: Int
    abstract val backActionId: Int
    var editable : Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.itemId = arguments?.getString("id").toString()
        viewModel.getUserRole()
        addLiveDataObservers()
    }

    private fun addLiveDataObservers() {
        viewModel.userRole.observe(viewLifecycleOwner) { role ->
            if (role != Role.getPlaceholder()) {
                if (role < Role.WORKER.ordinal) {
                    viewModel.getDataFromDatabase()
                } else {
                    initializeWorkerUI()
                }
            }
        }

        viewModel.readyToInitialize.observe(viewLifecycleOwner) { ready ->
            if (ready) {
                fillInData()
                if (editable) {
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
        cardSetNavigation?.cardBack?.root?.visibility = View.GONE
        cardSetNavigation?.cardEditBack?.root?.visibility = View.VISIBLE

        cardSetNavigation?.cardEditBack?.cardEdit?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", viewModel.itemId)

            findNavController().navigate(editActionId, bundle)
        }
        cardSetNavigation?.cardEditBack?.cardBack?.setOnClickListener {
            findNavController().navigate(backActionId)
        }
    }

    fun initializeWorkerUI() {
        cardSetNavigation?.cardBack?.root?.visibility = View.VISIBLE
        cardSetNavigation?.cardEditBack?.root?.visibility = View.GONE

        cardSetNavigation?.cardBack?.root?.setOnClickListener {
            findNavController().navigate(backActionId)
        }
        viewModel.setReadyToUnlock()
    }

    abstract fun fillInData()
}