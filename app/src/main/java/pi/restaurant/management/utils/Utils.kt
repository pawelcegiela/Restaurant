package pi.restaurant.management.utils

import android.content.Context
import android.widget.EditText
import androidx.fragment.app.Fragment
import pi.restaurant.management.R
import pi.restaurant.management.data.DiscountGroup
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

        fun checkUserPasswords(password1: String, password2: String): Precondition {
            if (password1 != password2) {
                return Precondition.PASSWORDS_DIFFER
            }

            val length = password1.length >= 8
            val uppercase = password1.contains(Regex("[A-Z]"))
            val lowercase = password1.contains(Regex("[a-z]"))
            val numbers = password1.contains(Regex("[0-9]"))
            //TODO Dodać znaki specjalne

            if (length && uppercase && lowercase && numbers) {
                return Precondition.OK
            }

            return Precondition.PASSWORD_TOO_WEAK
        }

        fun compareRoles(myRole: Int, newUserRole: Int): Precondition {
            if (myRole >= newUserRole) {
                return Precondition.TOO_LOW_ROLE
            }
            return Precondition.OK
        }

        fun formatAmountWithUnit(context: Context, amount: Int, unit: Int): String {
            return "$amount ${context.getString(Unit.getNameResById(unit))}"
        }

        fun formatDate(date: Date): String {
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            return sdf.format(date)
        }
    }
}