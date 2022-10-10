package pi.restaurant.management.ui.views

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import pi.restaurant.management.R

class YesNoDialog(context: Context?, title: Int, message: Int, positiveButtonFunction: DialogInterface.OnClickListener) :
    AlertDialog.Builder(context) {

    init {
        setTitle(title)
        setMessage(message)
        setPositiveButton(R.string.yes, positiveButtonFunction)
        setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
        create().show()
    }
}