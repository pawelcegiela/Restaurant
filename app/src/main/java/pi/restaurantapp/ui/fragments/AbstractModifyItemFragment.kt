package pi.restaurantapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pi.restaurantapp.R
import pi.restaurantapp.databinding.ToolbarNavigationModifyBinding
import pi.restaurantapp.model.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.enums.Precondition
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.ui.dialogs.RemovalDialog
import pi.restaurantapp.ui.dialogs.YesNoDialog
import pi.restaurantapp.ui.fragments.management.workers.EditWorkerFragment
import pi.restaurantapp.utils.UserInterfaceUtils

abstract class AbstractModifyItemFragment : Fragment() {
    abstract val viewModel: AbstractModifyItemViewModel

    abstract val linearLayout: LinearLayout
    abstract val progressBar: ProgressBar
    abstract val toolbarNavigation: ToolbarNavigationModifyBinding
    abstract var itemId: String
    abstract val nextActionId: Int
    abstract val saveMessageId: Int
    abstract val removeMessageId: Int
    open val lowestRole = Role.MANAGER.ordinal

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserRole()
        addLiveDataObservers()
    }

    fun addLiveDataObservers() {
        viewModel.userRole.observe(viewLifecycleOwner) { role ->
            if (role != Role.getPlaceholder()) {
                if (Role.isAtLeast(role, lowestRole)) {
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
                afterSave()
            }
        }
    }

    abstract fun initializeUI()

    private fun initializeWorkerUI() {
        Toast.makeText(requireContext(), R.string.no_permission, Toast.LENGTH_SHORT).show()
        findNavController().navigate(nextActionId)
    }

    open fun fillInData() {}

    open fun afterSave() {
        Toast.makeText(activity, getString(saveMessageId), Toast.LENGTH_SHORT).show()
        findNavController().navigate(nextActionId)
    }

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
            if (Role.isAtLeastAdmin(viewModel.userRole.value) && this !is EditWorkerFragment) {
                RemovalDialog(this, { disableItem() }, { removeFromDatabase() })
            } else {
                disableItem()
            }
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

    private fun disableItem() {
        if (!checkRemovePreconditions()) {
            return
        }
        YesNoDialog(requireContext(), R.string.warning, R.string.do_you_want_to_disable) { dialog, _ ->
            viewModel.disableItem()

            dialog.dismiss()
            Toast.makeText(activity, getString(R.string.this_item_has_been_disabled), Toast.LENGTH_SHORT).show()
            findNavController().navigate(nextActionId)
        }
    }

    private fun removeFromDatabase() {
        if (!checkRemovePreconditions()) {
            return
        }
        YesNoDialog(requireContext(), R.string.warning, R.string.do_you_want_to_remove) { dialog, _ ->
            viewModel.removeFromDatabase()

            dialog.dismiss()
            Toast.makeText(activity, getString(removeMessageId), Toast.LENGTH_SHORT).show()
            findNavController().navigate(nextActionId)
        }
    }

    open fun checkRemovePreconditions() = true

    fun finishLoading() {
        progressBar.visibility = View.GONE
        linearLayout.visibility = View.VISIBLE
    }
}