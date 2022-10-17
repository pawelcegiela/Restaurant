package pi.restaurant.management.ui.views

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.fragment.app.Fragment
import pi.restaurant.management.databinding.DialogRemovalBinding

class DialogRemoval(fragment: Fragment, private val disable: () -> (Unit), private val remove: () -> (Unit)) :
    Dialog(fragment.requireContext()) {
    val binding: DialogRemovalBinding = DialogRemovalBinding.inflate(layoutInflater, null, false)

    init {
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val width = (fragment.resources.displayMetrics.widthPixels * 0.8).toInt()
        val height = (fragment.resources.displayMetrics.heightPixels * 0.6).toInt() // TODO Workaround
        window?.setLayout(width, height)
        setContent()
        setListener()
        show()
    }

    private fun setContent() {
        binding.radioDisable.isChecked = true
    }

    private fun setListener() {
        binding.buttonContinue.setOnClickListener {
            if (binding.radioDisable.isChecked) {
                disable()
            } else {
                remove()
            }
            dismiss()
        }

    }
}