package pi.restaurantapp.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import pi.restaurantapp.databinding.DialogAddDiscountBinding

/**
 * Dialog with a list of discounts to add.
 */
class AddDiscountDialog(
    fragmentContext: Context,
    private val save: (String) -> (Unit)
) :
    Dialog(fragmentContext) {
    val binding: DialogAddDiscountBinding = DialogAddDiscountBinding.inflate(layoutInflater, null, false)

    init {
        setContentView(binding.root)
        setContent()
        setListener()
        show()
    }

    private fun setContent() {
        val width = (context.resources.displayMetrics.widthPixels * 0.9).toInt()
        window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun setListener() {
        binding.buttonSave.setOnClickListener {
            if (binding.editTextCode.text.isNotEmpty()) {
                save((binding.editTextCode.text).toString())
            }
            dismiss()
        }
    }
}