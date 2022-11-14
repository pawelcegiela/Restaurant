package pi.restaurantapp.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import pi.restaurantapp.databinding.DialogItemListBinding

abstract class AbstractItemListDialog(fragmentContext: Context, var itemList: List<String>, private val title: String) : Dialog(fragmentContext) {
    val binding: DialogItemListBinding = DialogItemListBinding.inflate(layoutInflater, null, false)

    fun initialize() {
        setContentView(binding.root)
        setContent()

        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, itemList)

        binding.textViewTitle.text = title
        binding.listViewItems.adapter = adapter
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })

        setListener()
        show()
    }

    private fun setContent() {
        val dialogWidth = Resources.getSystem().displayMetrics.widthPixels - 200
        val dialogHeight = Resources.getSystem().displayMetrics.heightPixels - 500
        window!!.setLayout(dialogWidth, dialogHeight)
    }

    abstract fun setListener()
}