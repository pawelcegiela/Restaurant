package pi.restaurantapp.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import pi.restaurantapp.databinding.DialogIngredientChangesBinding
import pi.restaurantapp.objects.enums.Unit

/**
 * Dialog responsible for changing of ingredient warehouse state amount.
 */
class IngredientChangesDialog(
    private val fragmentContext: Context,
    private val unit: Int,
    private val title: String,
    private val description: String,
    private val save: (Int) -> (kotlin.Unit)
) :
    Dialog(fragmentContext) {
    val binding: DialogIngredientChangesBinding = DialogIngredientChangesBinding.inflate(layoutInflater, null, false)

    init {
        setContentView(binding.root)
        setContent()
        setListener()
        show()
    }

    private fun setContent() {
        val width = (context.resources.displayMetrics.widthPixels * 0.9).toInt()
        window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

        binding.textViewTitle.text = title
        binding.textViewDescription.text = description
        binding.textViewUnit.text = Unit.getString(unit, fragmentContext)
    }

    private fun setListener() {
        binding.buttonSave.setOnClickListener {
            if (binding.editTextAmount.text.isNotEmpty()) {
                save((binding.editTextAmount.text).toString().toInt())
            }
            dismiss()
        }
    }
}