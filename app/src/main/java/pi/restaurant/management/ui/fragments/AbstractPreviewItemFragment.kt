package pi.restaurant.management.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurant.management.databinding.CardSetEditBackBinding
import pi.restaurant.management.objects.enums.Role
import pi.restaurant.management.logic.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.objects.SnapshotsPair

abstract class AbstractPreviewItemFragment : Fragment() {
    abstract val viewModel: AbstractPreviewItemViewModel

    abstract val linearLayout: LinearLayout
    abstract val progressBar: ProgressBar
    abstract val cardSetNavigation: CardSetEditBackBinding?
    abstract val editActionId: Int
    abstract val backActionId: Int
    lateinit var itemId: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemId = arguments?.getString("id").toString()
        viewModel.getUserRole()
        addLiveDataObservers()
    }

    private fun addLiveDataObservers() {
        viewModel.liveUserRole.observe(viewLifecycleOwner) { role ->
            if (role != Role.getPlaceholder()) {
                if (role < Role.WORKER.ordinal) {
                    viewModel.getDataFromDatabase(itemId)
                } else {
                    initializeWorkerUI()
                }
            }
        }

        viewModel.liveDataSnapshot.observe(viewLifecycleOwner) { snapshotPair ->
            if (snapshotPair.isReady()) {
                fillInData(snapshotPair)
                initializeUI()
            }
        }

        viewModel.liveReadyToUnlock.observe(viewLifecycleOwner) { ready ->
            if (ready) {
                linearLayout.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }

    open fun initializeUI() {
        cardSetNavigation?.cardBack?.root?.visibility = View.GONE
        cardSetNavigation?.cardEditBack?.root?.visibility = View.VISIBLE
        cardSetNavigation?.cardEditBack?.cardEdit?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", itemId)

            findNavController().navigate(editActionId, bundle)
        }
        cardSetNavigation?.cardEditBack?.cardBack?.setOnClickListener {
            findNavController().navigate(backActionId)
        }
    }

    fun initializeWorkerUI() {
        cardSetNavigation?.cardBack?.root?.setOnClickListener {
            findNavController().navigate(backActionId)
        }
        viewModel.liveReadyToUnlock.value = true
    }

    abstract fun fillInData(snapshotsPair: SnapshotsPair)
}