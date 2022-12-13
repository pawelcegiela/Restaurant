package pi.restaurantapp.ui.dialogs

import android.app.Dialog
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pi.restaurantapp.databinding.DialogRemovalBinding

/**
 * Dialog with a choice for administrators whether they want to disable or remove an item.
 */
class RemovalDialog(fragment: Fragment, disable: () -> (Unit), remove: () -> (Unit)) : Dialog(fragment.requireContext()) {
    val binding: DialogRemovalBinding = DialogRemovalBinding.inflate(layoutInflater, null, false)

    init {
        setContentView(binding.root)
        val width = (fragment.resources.displayMetrics.widthPixels * 0.9).toInt()
        window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

        binding.radioDisable.isChecked = true
        binding.buttonContinue.setOnClickListener {
            if (binding.radioDisable.isChecked) {
                disable()
            } else {
                remove()
            }
            dismiss()
        }
        show()
    }
}