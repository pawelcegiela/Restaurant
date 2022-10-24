package pi.restaurantapp.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import pi.restaurantapp.R

class YesNoDialog(context: Context?, title: Int, message: Int, positiveButtonFunction: DialogInterface.OnClickListener) :
    AlertDialog.Builder(context, R.style.RestaurantAlertDialog) {

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