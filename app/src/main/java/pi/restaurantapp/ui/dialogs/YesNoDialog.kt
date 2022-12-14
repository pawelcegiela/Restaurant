package pi.restaurantapp.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.Html
import pi.restaurantapp.R

/**
 * Custom dialog with yes and no options.
 */
class YesNoDialog(context: Context?, title: Int, message: Int, positiveButtonFunction: DialogInterface.OnClickListener) :
    AlertDialog.Builder(context, R.style.RestaurantAlertDialog) {

    init {
        setTitle(title)
        setMessage(Html.fromHtml("<font color='#C4CACF'>${context?.getString(message)}</font>", Html.FROM_HTML_MODE_LEGACY))
        setPositiveButton(
            Html.fromHtml("<font color='#C4CACF'>${context?.getString(R.string.yes)}</font>", Html.FROM_HTML_MODE_LEGACY),
            positiveButtonFunction
        )
        setNegativeButton(Html.fromHtml("<font color='#C4CACF'>${context?.getString(R.string.no)}</font>", Html.FROM_HTML_MODE_LEGACY)) { dialog, _ ->
            dialog.dismiss()
        }
        create().show()
    }
}