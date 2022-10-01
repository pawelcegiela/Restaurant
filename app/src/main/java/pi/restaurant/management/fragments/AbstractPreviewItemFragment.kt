package pi.restaurant.management.fragments

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurant.management.databinding.CardSetEditBackBinding
import pi.restaurant.management.enums.Role
import pi.restaurant.management.utils.SnapshotsPair

abstract class AbstractPreviewItemFragment : Fragment() {
    abstract val viewModel: AbstractPreviewItemViewModel

    abstract val linearLayout: LinearLayout
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
                    initializeUI()
                    viewModel.getDataFromDatabase(itemId)
                } else {
                    initializeWorkerUI()
                }
            }
        }

        viewModel.liveDataSnapshot.observe(viewLifecycleOwner) { snapshotPair ->
            if (snapshotPair.isReady()) {
                fillInData(snapshotPair)
                linearLayout.visibility = View.VISIBLE
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
        linearLayout.visibility = View.VISIBLE
    }

    abstract fun fillInData(snapshotsPair: SnapshotsPair)
}