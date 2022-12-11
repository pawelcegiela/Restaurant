package pi.restaurantapp

import org.junit.Assert.assertEquals
import org.junit.Test
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.address.AddressBasic
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.objects.data.ingredient.IngredientItem
import java.util.*

class FormattingUnitTest {
    @Test
    fun testSimpleFormat() {
        assertEquals(StringFormatUtils.format("test1", "test2"), "test1 test2")
    }

    @Test
    fun testFormatDateTime() {
        assertEquals(StringFormatUtils.formatDate(null), "")
        assertEquals(StringFormatUtils.formatDate(Date(1000000000000)), "09.09.2001")

        assertEquals(StringFormatUtils.formatTime(null), "")
        assertEquals(StringFormatUtils.formatTime(Date(100000)), "01:01")

        assertEquals(StringFormatUtils.formatDateTime(1000000000000L), "09.09.2001 03:46")
    }

    @Test
    fun testFormatPrice() {
        assertEquals(StringFormatUtils.formatPrice(null), "- zł")
        assertEquals(StringFormatUtils.formatPrice("3"), "3,00 zł")
        assertEquals(StringFormatUtils.formatPrice("2.10"), "2,10 zł")
        assertEquals(StringFormatUtils.formatPrice("-6.214"), "-6,21 zł")
    }

    @Test
    fun testFormatAddress() {
        val address = AddressBasic()
        assertEquals(StringFormatUtils.formatAddress(null), "")
        assertEquals(StringFormatUtils.formatAddress(address), " \n ")

        address.city = "Wroclaw"
        address.postalCode = "50-001"
        address.street = "Plac Grunwaldzki"
        address.houseNumber = "12"
        assertEquals(StringFormatUtils.formatAddress(address), "Plac Grunwaldzki 12\n50-001 Wroclaw")

        address.flatNumber = "3"
        assertEquals(StringFormatUtils.formatAddress(address), "Plac Grunwaldzki 12 / 3\n50-001 Wroclaw")
    }

    @Test
    fun testFormatDishChanges() {
        val dishItem = DishItem()
        dishItem.usedPossibleIngredients = arrayListOf(
            IngredientItem("", "Name1", 0),
            IngredientItem("", "Name2", 0)
        )

        dishItem.unusedOtherIngredients = arrayListOf(
            IngredientItem("", "NameA", 0),
            IngredientItem("", "NameB", 0)
        )

        assertEquals(
            StringFormatUtils.formatDishChanges(dishItem),
            "+   Name1\n+   Name2\n-   NameA\n-   NameB"
        )
    }

    @Test
    fun testFormatDishItemDetails() {
        val dishItem = DishItem()
        dishItem.amount = 4
        dishItem.finalPrice = "21.22"

        assertEquals(StringFormatUtils.formatDishItemDetails(dishItem), "x4,   21,22 zł")
    }

    @Test
    fun testFormatOpeningHours() {
        assertEquals(StringFormatUtils.formatOpeningHours(Date(1000000), Date(2000000)), "01:16  -  01:33")
        assertEquals(StringFormatUtils.formatOpeningHours(Date(100000), Date(20000000)), "01:01  -  06:33")
    }

    @Test
    fun testFormatDateTimeFromIntegers() {
        assertEquals(StringFormatUtils.formatDateFromIntegers(1, 0, 2022), "01.01.2022")
        assertEquals(StringFormatUtils.formatDateFromIntegers(20, 11, 2000), "20.12.2000")
        assertEquals(StringFormatUtils.formatDateFromIntegers(5, 5, 2020), "05.06.2020")

        assertEquals(StringFormatUtils.formatTimeFromIntegers(5, 5), "05:05")
        assertEquals(StringFormatUtils.formatTimeFromIntegers(0, 55), "00:55")
        assertEquals(StringFormatUtils.formatTimeFromIntegers(23, 3), "23:03")
    }
}