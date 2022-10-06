package pi.restaurant.management.utils

import pi.restaurant.management.objects.data.openinghours.OpeningHoursBasic
import pi.restaurant.management.objects.enums.Precondition

class PreconditionUtils {
    companion object {
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

        fun checkOpeningHoursError(data: OpeningHoursBasic) : Precondition {
            if (data.isError) {
                return Precondition.OPENING_HOURS_FORMAT
            }
            return Precondition.OK
        }
    }
}