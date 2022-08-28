package pi.restaurant.management.data

import java.util.*
import kotlin.collections.ArrayList

class DiscountGroup : BaseDataObject {
    var availableDiscounts: ArrayList<String> = ArrayList()
    var assignedDiscounts: ArrayList<String> = ArrayList()
    var usedDiscounts: ArrayList<String> = ArrayList()

    var type: Int = 0
    var amount: Double = 0.0
    lateinit var expirationDate: Date

    @Suppress("unused")
    constructor()

    constructor(
        availableDiscounts: ArrayList<String>,
        assignedDiscounts: ArrayList<String>,
        usedDiscounts: ArrayList<String>,
        id: String,
        type: Int,
        amount: Double,
        expirationDate: Date
    ) {
        this.availableDiscounts = availableDiscounts
        this.assignedDiscounts = assignedDiscounts
        this.usedDiscounts = usedDiscounts
        this.id = id
        this.type = type
        this.amount = amount
        this.expirationDate = expirationDate
    }
}