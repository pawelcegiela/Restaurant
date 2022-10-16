package pi.restaurant.management.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurant.management.R
import pi.restaurant.management.databinding.ToolbarNavigationModifyBinding
import pi.restaurant.management.model.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.objects.data.SplitDataObject
import pi.restaurant.management.objects.enums.Precondition
import pi.restaurant.management.objects.enums.Role
import pi.restaurant.management.utils.UserInterfaceUtils

abstract class AbstractModifyItemFragment : Fragment() {
    abstract val viewModel: AbstractModifyItemViewModel

    abstract val linearLayout: LinearLayout
    abstract val progressBar: ProgressBar
    abstract val toolbarNavigation: ToolbarNavigationModifyBinding
    abstract var itemId: String
    abstract val nextActionId: Int
    abstract val saveMessageId: Int
    abstract val removeMessageId: Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserRole()
        addLiveDataObservers()
    }

    fun addLiveDataObservers() {
        viewModel.userRole.observe(viewLifecycleOwner) { role ->
            if (role != Role.getPlaceholder()) {
                if (Role.isAtLeastManager(role)) {
                    initializeUI()
                    viewModel.itemId = itemId
                    if (itemId.isNotEmpty() && viewModel.shouldGetDataFromDatabase()) {
                        viewModel.getDataFromDatabase()
                    }
                } else {
                    initializeWorkerUI()
                }
            }
        }

        viewModel.readyToInitialize.observe(viewLifecycleOwner) { ready ->
            if (ready) {
                fillInData()
                finishLoading()
            }
        }

        viewModel.saveStatus.observe(viewLifecycleOwner) { saved ->
            if (saved) {
                Toast.makeText(activity, getString(saveMessageId), Toast.LENGTH_SHORT).show()
                findNavController().navigate(nextActionId)
            }
        }
    }

    abstract fun initializeUI()

    private fun initializeWorkerUI() {
        Toast.makeText(requireContext(), R.string.no_permission, Toast.LENGTH_SHORT).show()
        findNavController().navigate(nextActionId)
    }

    open fun fillInData() {}

    fun setNavigationCardsSave() {
        toolbarNavigation.root.visibility = View.VISIBLE
        toolbarNavigation.cardSave.root.visibility = View.VISIBLE
        toolbarNavigation.cardSave.root.setOnClickListener {
            if (!UserInterfaceUtils.checkRequiredFields(getEditTextMap(), this)) {
                return@setOnClickListener
            }

            saveToDatabase()
        }
    }

    open fun setNavigationCardsSaveRemove() {
        if (!Role.isAtLeastExecutive(viewModel.userRole.value)) {
            setNavigationCardsSave()
            return
        }
        toolbarNavigation.root.visibility = View.VISIBLE
        toolbarNavigation.cardSaveRemove.root.visibility = View.VISIBLE
        toolbarNavigation.cardSaveRemove.cardSave2.setOnClickListener {
            if (!UserInterfaceUtils.checkRequiredFields(getEditTextMap(), this)) {
                return@setOnClickListener
            }

            saveToDatabase()
        }

        toolbarNavigation.cardSaveRemove.cardRemove.setOnClickListener {
            removeFromDatabase()
        }
    }


    abstract fun getEditTextMap(): Map<EditText, Int>

    open fun saveToDatabase() {
        val data = getDataObject()

        val precondition = checkSavePreconditions(data)
        if (precondition != Precondition.OK) {
            Toast.makeText(activity, getString(precondition.nameRes), Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.saveToDatabase(data)
    }

    abstract fun getDataObject(): SplitDataObject

    open fun checkSavePreconditions(data: SplitDataObject): Precondition {
        return Precondition.OK
    }

    private fun removeFromDatabase() {
        if (!checkRemovePreconditions()) {
            return
        }
        val dialogBuilder = AlertDialog.Builder(this.context)
        dialogBuilder.setTitle(getString(R.string.warning))
        dialogBuilder.setMessage(getString(R.string.do_you_want_to_remove))
        dialogBuilder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            viewModel.removeFromDatabase()

            dialog.dismiss()
            Toast.makeText(activity, getString(removeMessageId), Toast.LENGTH_SHORT).show()
            findNavController().navigate(nextActionId)
        }
        dialogBuilder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
        dialogBuilder.create().show()
    }

    open fun checkRemovePreconditions() = true

    fun finishLoading() {
        progressBar.visibility = View.GONE
        linearLayout.visibility = View.VISIBLE
    }
}