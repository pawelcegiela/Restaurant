package pi.restaurantapp

import junit.framework.Assert.assertEquals
import org.junit.Test
import pi.restaurantapp.logic.utils.ComputingUtils
import pi.restaurantapp.objects.data.delivery.DeliveryBasic
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.objects.enums.CollectionType
import pi.restaurantapp.objects.enums.DiscountValueType
import java.util.*

class ComputingUnitTest {
    @Test
    fun testTimeAfterDate() {

    }

    @Test
    fun testTimeFromString() {
        val date1 = ComputingUtils.getTimeFromString("21:55")
        val date2 = ComputingUtils.getTimeFromString("0:00")
        val date3 = ComputingUtils.getTimeFromString("1:50")

        // Wyjaśnienie: ComputingUtils liczy godzinę w czasie środkowoeuropejskim, natomiast wartość
        // w konstruktorze Date() jest liczbą milisekund od 1.01.1970 00:00 GMT
        assertEquals(date1, Date(60000 * (55 + 60 * 20)))
        assertEquals(date2, Date(60000 * (0 + 60 * -1)))
        assertEquals(date3, Date(60000 * (50 + 60 * 0)))
    }

    @Test
    fun testDateTimeFromString() {
        val date1 = ComputingUtils.getDateTimeFromString("01.01.1970 21:55")
        val date2 = ComputingUtils.getDateTimeFromString("02.01.1970 0:00")

        // Wyjaśnienie: ComputingUtils liczy godzinę w czasie środkowoeuropejskim, natomiast wartość
        // w konstruktorze Date() jest liczbą milisekund od 1.01.1970 00:00 GMT
        assertEquals(date1, Date(60000 * (55 + 60 * 20)))
        assertEquals(date2, Date(60000 * (0 + 60 * 23)))
    }

    @Test
    fun testInitialExpirationDate() {
        val date = ComputingUtils.getInitialExpirationDate()
        val weekInMilliseconds = 1000 * 60 * 60 * 24 * 7

        assert(date.time - Date().time <= weekInMilliseconds)
    }

    @Test
    fun testFullOrderPrice() {
        val dishesList = arrayListOf(DishItem("20"), DishItem("11"), DishItem("1"))

        var collectionTypeId = CollectionType.DELIVERY.ordinal

        val deliveryOptions = DeliveryBasic(
            available = true,
            minimumPrice = "30",
            extraDeliveryFee = "10",
            freeDeliveryAvailable = false,
            minimumPriceFreeDelivery = "35"
        )

        assertEquals(ComputingUtils.countFullOrderPrice(dishesList, collectionTypeId, deliveryOptions), "42")

        deliveryOptions.freeDeliveryAvailable = true
        assertEquals(ComputingUtils.countFullOrderPrice(dishesList, collectionTypeId, deliveryOptions), "42")

        deliveryOptions.minimumPriceFreeDelivery = "30"
        assertEquals(ComputingUtils.countFullOrderPrice(dishesList, collectionTypeId, deliveryOptions), "32")

        deliveryOptions.freeDeliveryAvailable = false
        collectionTypeId = CollectionType.SELF_PICKUP.ordinal
        assertEquals(ComputingUtils.countFullOrderPrice(dishesList, collectionTypeId, deliveryOptions), "32")
    }

    @Test
    fun testPriceAfterDiscount() {
        var discount = DiscountBasic("10", DiscountValueType.ABSOLUTE.ordinal, true, "20")
        assertEquals(ComputingUtils.countPriceAfterDiscount("30", discount), "20")
        assertEquals(ComputingUtils.countPriceAfterDiscount("15", discount), "15")

        discount = DiscountBasic("10", DiscountValueType.ABSOLUTE.ordinal, false, "20")
        assertEquals(ComputingUtils.countPriceAfterDiscount("15", discount), "5")
        assertEquals(ComputingUtils.countPriceAfterDiscount("5", discount), "0")

        discount = DiscountBasic("10", DiscountValueType.RELATIVE.ordinal, false, "20")
        assertEquals(ComputingUtils.countPriceAfterDiscount("100", discount), "90.00")
        assertEquals(ComputingUtils.countPriceAfterDiscount("5", discount), "4.50")
    }
}