package pi.restaurant.management.fragments

import android.app.AlertDialog
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurant.management.R
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.SplitDataObject
import pi.restaurant.management.databinding.CardSetNavigationModifyBinding
import pi.restaurant.management.databinding.CardViewSaveRemoveBackBinding
import pi.restaurant.management.enums.Precondition
import pi.restaurant.management.enums.Role
import pi.restaurant.management.utils.SnapshotsPair
import pi.restaurant.management.utils.Utils

abstract class AbstractModifyItemFragment : Fragment() {
    abstract val viewModel: AbstractModifyItemViewModel

    abstract val linearLayout: LinearLayout
    abstract val progressBar: ProgressBar
    abstract val cardSetNavigation: CardSetNavigationModifyBinding
    abstract var itemId: String
    abstract val nextActionId: Int
    abstract val saveMessageId: Int
    abstract val removeMessageId: Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserRole()
        addLiveDataObservers()
    }

    private fun addLiveDataObservers() {
        viewModel.liveUserRole.observe(viewLifecycleOwner) { role ->
            if (role != Role.getPlaceholder()) {
                if (role < Role.WORKER.ordinal) {
                    initializeUI()
                    if (itemId.isNotEmpty()) {
                        viewModel.getDataFromDatabase(itemId)
                    }
                } else {
                    initializeWorkerUI()
                }
            }
        }

        viewModel.liveDataSnapshot.observe(viewLifecycleOwner) { snapshotPair ->
            if (snapshotPair.isReady()) {
                fillInData(snapshotPair)
                finishLoading()
            }
        }

        viewModel.liveSaveStatus.observe(viewLifecycleOwner) {
            Toast.makeText(activity, getString(saveMessageId), Toast.LENGTH_SHORT).show()
            findNavController().navigate(nextActionId)
        }
    }

    abstract fun initializeUI()

    private fun initializeWorkerUI() {
        Toast.makeText(requireContext(), R.string.no_permission, Toast.LENGTH_SHORT).show()
        findNavController().navigate(nextActionId)
    }

    open fun fillInData(snapshotsPair: SnapshotsPair) {}

    fun setNavigationCardsSave() {
        cardSetNavigation.cardSaveBack.cardSave.setOnClickListener {
            if (!Utils.checkRequiredFields(getEditTextMap(), this)) {
                return@setOnClickListener
            }

            saveToDatabase()
        }

        cardSetNavigation.cardSaveBack.cardBack.setOnClickListener {
            findNavController().navigate(nextActionId)
        }
    }

    open fun setNavigationCardsSaveRemove() {
        cardSetNavigation.cardSaveBack.root.visibility = View.GONE
        cardSetNavigation.cardSaveRemoveBack.root.visibility = View.VISIBLE
        cardSetNavigation.cardSaveRemoveBack.cardSave.setOnClickListener {
            if (!Utils.checkRequiredFields(getEditTextMap(), this)) {
                return@setOnClickListener
            }

            saveToDatabase()
        }

        cardSetNavigation.cardSaveRemoveBack.cardRemove.setOnClickListener {
            viewModel.liveDataSnapshot.value?.let { snapshotsPair -> removeFromDatabase(snapshotsPair) }
        }

        cardSetNavigation.cardSaveRemoveBack.cardBack.setOnClickListener {
            findNavController().navigate(nextActionId)
        }
    }


    abstract fun getEditTextMap(): Map<EditText, Int>

    open fun saveToDatabase() {
        val data = getDataObject()

        val precondition = checkSavePreconditions(data.basic)
        if (precondition != Precondition.OK) {
            Toast.makeText(activity, getString(precondition.nameRes), Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.saveToDatabase(data)
    }

    abstract fun getDataObject(): SplitDataObject

    open fun checkSavePreconditions(data: AbstractDataObject): Precondition {
        return Precondition.OK
    }

    private fun removeFromDatabase(snapshotsPair: SnapshotsPair) {
        val dialogBuilder = AlertDialog.Builder(this.context)
        dialogBuilder.setTitle(getString(R.string.warning))
        dialogBuilder.setMessage(getString(R.string.do_you_want_to_remove))
        dialogBuilder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            for (snapshot in snapshotsPair.basic!!.children) {
                snapshot.ref.removeValue()
            }
            for (snapshot in snapshotsPair.details!!.children) {
                snapshot.ref.removeValue()
            }

            dialog.dismiss()
            Toast.makeText(activity, getString(removeMessageId), Toast.LENGTH_SHORT).show()
            findNavController().navigate(nextActionId)
        }
        dialogBuilder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
        dialogBuilder.create().show()
    }

    fun finishLoading() {
        progressBar.visibility = View.GONE
        linearLayout.visibility = View.VISIBLE
    }
}