package pi.restaurantapp.logic.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import pi.restaurantapp.R
import pi.restaurantapp.databinding.CardViewWideBinding

/**
 * Class responsible for changing or checking user interface settings,
 * such as checking if all required fields have been filled or converting drawable to bitmap.
 * It separates common logic from other layers.
 */
class UserInterfaceUtils {
    companion object {
        fun checkRequiredFields(map: Map<EditText, Int>, fragment: Fragment): Boolean {
            var allFilled = true
            map.forEach {
                val editText = it.key
                val resourceId = it.value

                if (editText.text.trim().isEmpty()) {
                    editText.error =
                        fragment.getString(R.string.is_required, fragment.getString(resourceId))
                    allFilled = false
                }
            }
            return allFilled
        }

        fun setCardView(cardView: CardViewWideBinding, drawableId: Int, stringId: Int, navController: NavController, actionId: Int) {
            cardView.imageIcon.setImageResource(drawableId)
            cardView.textViewName.setText(stringId)
            cardView.root.setOnClickListener {
                navController.navigate(actionId)
            }
        }

        fun dpToPx(dp: Int, resources: Resources): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics)
        }

        fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
            convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

        private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
            if (sourceDrawable == null) {
                return null
            }
            return if (sourceDrawable is BitmapDrawable) {
                sourceDrawable.bitmap
            } else {
                val constantState = sourceDrawable.constantState ?: return null
                val drawable = constantState.newDrawable().mutate()
                val bitmap: Bitmap = Bitmap.createBitmap(
                    drawable.intrinsicWidth, drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bitmap
            }
        }
    }
}