package pi.restaurantapp

import org.junit.Assert.assertEquals
import org.junit.Test
import pi.restaurantapp.logic.utils.PreconditionUtils
import pi.restaurantapp.objects.data.delivery.DeliveryBasic
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.objects.data.openinghours.OpeningHoursBasic
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails
import pi.restaurantapp.objects.enums.CollectionType
import pi.restaurantapp.objects.enums.Precondition
import pi.restaurantapp.objects.enums.Role

class PreconditionsUnitTest {
    @Test
    fun testUserPassword() {
        assertEquals(PreconditionUtils.checkUserPasswords("password1", "password2"), Precondition.PASSWORDS_DIFFER)
        assertEquals(PreconditionUtils.checkUserPasswords("password", "password"), Precondition.PASSWORD_TOO_WEAK)
        assertEquals(PreconditionUtils.checkUserPasswords("Pp1", "Pp1"), Precondition.PASSWORD_TOO_WEAK)
        assertEquals(PreconditionUtils.checkUserPasswords("Password1", "Password1"), Precondition.OK)
    }

    @Test
    fun testCompareRoles() {
        assertEquals(PreconditionUtils.compareRoles(Role.MANAGER.ordinal, Role.WORKER.ordinal), Precondition.OK)
        assertEquals(PreconditionUtils.compareRoles(Role.MANAGER.ordinal, Role.MANAGER.ordinal), Precondition.TOO_LOW_ROLE)
        assertEquals(PreconditionUtils.compareRoles(Role.EXECUTIVE.ordinal, Role.ADMIN.ordinal), Precondition.TOO_LOW_ROLE)
    }

    @Test
    fun testOpeningHoursError() {
        val openingHours = OpeningHoursBasic()

        openingHours.isError = true
        assertEquals(PreconditionUtils.checkOpeningHoursError(openingHours), Precondition.INVALID_OPENING_HOURS)

        openingHours.isError = false
        assertEquals(PreconditionUtils.checkOpeningHoursError(openingHours), Precondition.OK)
    }

    @Test
    fun testCheckOrder() {
        val orderBasic = OrderBasic()
        val orderDetails = OrderDetails()
        orderBasic.collectionType = CollectionType.DELIVERY.ordinal

        val deliveryOptions = DeliveryBasic(
            available = true,
            minimumPrice = "30",
            extraDeliveryFee = "10",
            freeDeliveryAvailable = false,
            minimumPriceFreeDelivery = "35"
        )

        assertEquals(PreconditionUtils.checkOrder(orderBasic, orderDetails, deliveryOptions), Precondition.EMPTY_ORDER)

        orderDetails.dishes["testItem"] = DishItem()
        assertEquals(PreconditionUtils.checkOrder(orderBasic, orderDetails, deliveryOptions), Precondition.TOO_LOW_VALUE_DELIVERY)

        orderBasic.value = "40"
        assertEquals(PreconditionUtils.checkOrder(orderBasic, orderDetails, deliveryOptions), Precondition.OK)
    }
}