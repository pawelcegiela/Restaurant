package pi.restaurantapp.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import pi.restaurantapp.R
import pi.restaurantapp.databinding.CardViewWideBinding

class UserInterfaceUtils {
    companion object {
        fun setRecyclerSize(recyclerView: RecyclerView, size: Int, context: Context) {
            val itemSize = 50
            val layoutParams = LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                (itemSize * context.resources.displayMetrics.density * size).toInt()
            )
            recyclerView.layoutParams = layoutParams
        }

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
    }
}