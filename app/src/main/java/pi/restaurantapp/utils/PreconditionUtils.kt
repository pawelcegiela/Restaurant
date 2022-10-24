package pi.restaurantapp.utils

import pi.restaurantapp.objects.data.delivery.DeliveryBasic
import pi.restaurantapp.objects.data.openinghours.OpeningHoursBasic
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails
import pi.restaurantapp.objects.enums.CollectionType
import pi.restaurantapp.objects.enums.Precondition
import java.math.BigDecimal

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
            //TODO Add special characters

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

        fun checkOpeningHoursError(data: OpeningHoursBasic): Precondition {
            if (data.isError) {
                return Precondition.INVALID_OPENING_HOURS
            }
            return Precondition.OK
        }

        fun checkOrder(orderBasic: OrderBasic, orderDetails: OrderDetails, deliveryOptions: DeliveryBasic?): Precondition {
            if (orderDetails.dishes.isEmpty()) {
                return Precondition.EMPTY_ORDER
            }
            if (orderBasic.collectionType == CollectionType.DELIVERY.ordinal && BigDecimal(orderBasic.value)
                < BigDecimal(deliveryOptions?.minimumPrice ?: "0.0")) {
                return Precondition.TOO_LOW_VALUE_DELIVERY
            }
            return Precondition.OK
        }
    }
}