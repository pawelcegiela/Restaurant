package pi.restaurant.management.ui.dialogs

import android.app.Dialog
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pi.restaurant.management.databinding.DialogFilterBinding

class FilterDialog(
    val fragment: Fragment,
    showActive: Boolean,
    showDisabled: Boolean,
    onSave: (Boolean, Boolean) -> (Unit)
) :
    Dialog(fragment.requireContext()) {
    val binding: DialogFilterBinding = DialogFilterBinding.inflate(layoutInflater, null, false)

    init {
        setContentView(binding.root)
        val width = (fragment.resources.displayMetrics.widthPixels * 0.9).toInt()
        window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

        binding.checkBoxActive.isChecked = showActive
        binding.checkBoxDisabled.isChecked = showDisabled

        binding.buttonSave.setOnClickListener {
            onSave(binding.checkBoxActive.isChecked, binding.checkBoxDisabled.isChecked)
            dismiss()
        }
        show()
    }
}