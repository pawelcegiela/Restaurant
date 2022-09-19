package pi.restaurant.management.utils

import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.R
import pi.restaurant.management.data.DiscountGroup
import pi.restaurant.management.data.OpeningHours
import pi.restaurant.management.enums.Precondition
import pi.restaurant.management.enums.Unit
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Utils {
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

        fun createDiscounts(code: String, number: Int, startNumber: Int): ArrayList<String> {
            val discounts = ArrayList<String>()
            for (i in (startNumber + 1)..(startNumber + number)) {
                discounts.add("$code#$i")
            }
            return discounts
        }

        fun getNumberOfDiscounts(discount: DiscountGroup): String {
            return discount.availableDiscounts.size.toString() + " / " +
                    discount.assignedDiscounts.size.toString() + " / " +
                    discount.usedDiscounts.size.toString()
        }

        fun getDiscountAmount(discount: DiscountGroup): String {
            return discount.amount.toString() + discount.type.toString()
        }

        fun setRecyclerSize(recyclerView: RecyclerView, size: Int, context: Context) {
            val itemSize = 60
            val layoutParams = LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                (itemSize * context.resources.displayMetrics.density * size).toInt()
            )
            recyclerView.layoutParams = layoutParams
        }
    }
}