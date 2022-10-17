package pi.restaurant.management.ui.views

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.fragment.app.Fragment
import pi.restaurant.management.databinding.DialogFilterBinding

class DialogFilter(
    val fragment: Fragment,
    private val showActive: Boolean,
    private val showDisabled: Boolean,
    private val onSave: (Boolean, Boolean) -> (Unit)
) :
    Dialog(fragment.requireContext()) {
    val binding: DialogFilterBinding = DialogFilterBinding.inflate(layoutInflater, null, false)

    init {
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContent()
        setListener()
        show()
    }

    private fun setContent() {
        binding.checkBoxActive.isChecked = showActive
        binding.checkBoxDisabled.isChecked = showDisabled
    }

    private fun setListener() {
        binding.cardSave.setOnClickListener {
            onSave(binding.checkBoxActive.isChecked, binding.checkBoxDisabled.isChecked)
            dismiss()
        }
    }
}