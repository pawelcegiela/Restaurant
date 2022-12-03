package pi.restaurantapp

import junit.framework.Assert.assertEquals
import org.junit.Test
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.logic.utils.ComputingUtils
import java.util.Date

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
}