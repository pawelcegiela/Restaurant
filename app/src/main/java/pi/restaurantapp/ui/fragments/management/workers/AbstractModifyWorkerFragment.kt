package pi.restaurantapp.ui.fragments.management.workers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentModifyWorkerBinding
import pi.restaurantapp.viewmodels.fragments.management.workers.AbstractModifyWorkerViewModel
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.enums.Precondition
import pi.restaurantapp.ui.fragments.AbstractModifyItemFragment
import pi.restaurantapp.logic.utils.PreconditionUtils

abstract class AbstractModifyWorkerFragment : AbstractModifyItemFragment() {
    private var _binding: FragmentModifyWorkerBinding? = null
    val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation get() = binding.toolbarNavigation
    override var itemId = ""

    private val _viewModel get() = viewModel as AbstractModifyWorkerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyWorkerBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.fragment = this
        binding.lifecycleOwner = this
        linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun getDataObject(): SplitDataObject {
        return SplitDataObject(_viewModel.itemId, _viewModel.item.value!!.basic, _viewModel.item.value!!.details)
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextFirstName] = R.string.first_name
        map[binding.editTextLastName] = R.string.last_name
        map[binding.editTextEmail] = R.string.e_mail
        map[binding.editTextContactPhone] = R.string.contact_phone
        return map
    }

    override fun checkSavePreconditions(data: SplitDataObject): Precondition {
        if (super.checkSavePreconditions(data) != Precondition.OK) {
            return super.checkSavePreconditions(data)
        }
        return PreconditionUtils.compareRoles(viewModel.userRole.value!!, (data.basic as UserBasic).role)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}