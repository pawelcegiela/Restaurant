package pi.restaurantapp

import junit.framework.Assert.assertEquals
import org.junit.Test
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.logic.utils.ComputingUtils

class DiscountRewardsUnitTest {
    @Test
    fun testNotEmptyList() {
        val orders = arrayListOf(
            OrderBasic("", "user1", "10.00"),
            OrderBasic("", "user4", "2.50"),
            OrderBasic("", "user1", "10.00"),
            OrderBasic("", "user2", "5.00"),
            OrderBasic("", "user1", "20.55"),
            OrderBasic("", "user3", "10.00"),
            OrderBasic("", "user3", "10.00"),
            OrderBasic("", "user2", "1.00"),
            OrderBasic("", "user5", "20.00")
        )

        val rewards = ComputingUtils.countDiscountRewards(orders, 100, 5)
        assertEquals(rewards["user1"], 46)
        assertEquals(rewards["user2"], 6)
        assertEquals(rewards["user3"], 23)
        assertEquals(rewards["user4"], null)
        assertEquals(rewards["user5"], 23)
        println(rewards)
    }

    @Test
    fun testEmptyList() {
        val orders = ArrayList<OrderBasic>()
        val rewards = ComputingUtils.countDiscountRewards(orders, 100, 5)
        assert(rewards.isEmpty())
    }

    @Test
    fun testBigMinimumDiscountValue() {
        val orders = arrayListOf(
            OrderBasic("", "user1", "10.00"),
            OrderBasic("", "user4", "2.50"),
            OrderBasic("", "user1", "10.00"),
            OrderBasic("", "user2", "5.00"),
            OrderBasic("", "user1", "20.55"),
            OrderBasic("", "user3", "10.00"),
            OrderBasic("", "user3", "10.00"),
            OrderBasic("", "user2", "1.00"),
            OrderBasic("", "user5", "20.00")
        )

        val rewards = ComputingUtils.countDiscountRewards(orders, 100, 35)
        assertEquals(rewards["user1"], 100)
        println(rewards)
    }

    @Test
    fun testIncorrectMinimumDiscountValue() {
        val orders = arrayListOf(
            OrderBasic("", "user1", "10.00")
        )

        val rewards = ComputingUtils.countDiscountRewards(orders, 100, 105)
        assert(rewards.isEmpty())
    }
}