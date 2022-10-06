package pi.restaurant.management.listeners

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import pi.restaurant.management.R

abstract class AbstractAddSubItemButtonListener(
    private val context: Context,
    var itemList: List<String>,
) :
    View.OnClickListener {

    override fun onClick(p0: View?) {
        val dialog = Dialog(context)
        val dialogWidth = Resources.getSystem().displayMetrics.widthPixels - 200
        val dialogHeight = Resources.getSystem().displayMetrics.heightPixels - 500

        dialog.setContentView(R.layout.dialog_searchable_spinner)
        dialog.window!!.setLayout(dialogWidth, dialogHeight)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val editText = dialog.findViewById<EditText>(R.id.editTextSearch)
        val listView = dialog.findViewById<ListView>(R.id.listViewItems)

        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, itemList)
        listView.adapter = adapter

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })

        setOnItemClickListener(dialog, listView)
    }

    abstract fun setOnItemClickListener(dialog: Dialog, listView: ListView)
}