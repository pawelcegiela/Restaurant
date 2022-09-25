package pi.restaurant.management.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurant.management.R
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.SplitDataObject
import pi.restaurant.management.enums.Precondition
import pi.restaurant.management.enums.Role
import pi.restaurant.management.utils.SnapshotsPair
import pi.restaurant.management.utils.Utils

abstract class AbstractModifyItemFragment : Fragment() {
    abstract val viewModel: AbstractModifyItemViewModel
    var myRole: Int = 3

    abstract val linearLayout: LinearLayout
    abstract val progressBar: ProgressBar
    abstract val saveButton: Button
    abstract val removeButton: Button?
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
            if (role < Role.WORKER.ordinal) {
                unlockUI()
                initializeUI()
                if (itemId.isNotEmpty()) {
                    viewModel.getDataFromDatabase(itemId)
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

    private fun unlockUI() {
        for (view in linearLayout.children) {
            view.isEnabled = true
        }
        saveButton.text = getText(R.string.save)
        removeButton?.text = getText(R.string.remove_item)
    }

    abstract fun initializeUI()

    open fun fillInData(snapshotsPair: SnapshotsPair) {}

    fun setSaveButtonListener() {
        saveButton.setOnClickListener {
            if (!Utils.checkRequiredFields(getEditTextMap(), this)) {
                return@setOnClickListener
            }

            saveToDatabase()
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

    open fun setRemoveButtonListener() {
        removeButton?.setOnClickListener {
            viewModel.liveDataSnapshot.value?.let { snapshotsPair -> removeFromDatabase(snapshotsPair) }
        }
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