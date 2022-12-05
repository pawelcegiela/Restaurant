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
import pi.restaurantapp.logic.utils.UserInterfaceUtils
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.ui.dialogs.RemovalDialog
import pi.restaurantapp.ui.dialogs.YesNoDialog
import pi.restaurantapp.ui.fragments.management.workers.EditWorkerFragment
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel

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
                if (Role.isAtLeast(role, viewModel.lowestRole)) {
                    initializeUI()
                    viewModel.itemId = itemId
                    if (viewModel.itemId.isNotEmpty() && viewModel.shouldGetDataFromDatabase()) {
                        viewModel.getDataFromDatabase()
                    } else if (viewModel.itemId.isEmpty() && viewModel.shouldGetDataFromDatabase()) {
                        viewModel.createItem()
                    }
                } else {
                    Toast.makeText(requireContext(), R.string.no_permission, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(nextActionId)
                }
                initializeNavigationToolbar()
            }
        }

        viewModel.readyToInitialize.observe(viewLifecycleOwner) { ready ->
            if (ready) {
                finishLoading()
            }
        }

        viewModel.saveStatus.observe(viewLifecycleOwner) { saved ->
            if (saved) {
                afterSave()
            }
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { messageId ->
            Toast.makeText(activity, getString(messageId), Toast.LENGTH_SHORT).show()
        }
    }

    abstract fun initializeUI()

    open fun afterSave() {
        Toast.makeText(activity, getString(saveMessageId), Toast.LENGTH_SHORT).show()
        findNavController().navigate(nextActionId)
    }

    fun initializeNavigationToolbar() {
        toolbarNavigation.cardSave.root.setOnClickListener {
            if (!UserInterfaceUtils.checkRequiredFields(getEditTextMap(), this)) {
                return@setOnClickListener
            }

            viewModel.saveToDatabase()
        }

        toolbarNavigation.cardSaveRemove.cardSave2.setOnClickListener {
            if (!UserInterfaceUtils.checkRequiredFields(getEditTextMap(), this)) {
                return@setOnClickListener
            }

            viewModel.saveToDatabase()
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