package pi.restaurant.management.objects.data.discount

import pi.restaurant.management.objects.data.AbstractDataObject
import java.util.*

class DiscountBasic : AbstractDataObject {
    var amount: String = "0.0"
    var type: Int = 0
    var hasThreshold: Boolean = false
    var thresholdValue: String = "0.0"
    var creationDate: Date = Date()
    var expirationDate: Date = Date()
    var availableDiscounts: ArrayList<String> = ArrayList()
    var assignedDiscounts: ArrayList<String> = ArrayList()
    var redeemedDiscounts: ArrayList<String> = ArrayList()
    var disabled: Boolean = false

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        amount: String,
        type: Int,
        hasThreshold: Boolean,
        thresholdValue: String,
        creationDate: Date,
        expirationDate: Date,
        availableDiscounts: ArrayList<String>,
        assignedDiscounts: ArrayList<String>,
        redeemedDiscounts: ArrayList<String>
    ) {
        this.id = id
        this.amount = amount
        this.type = type
        this.hasThreshold = hasThreshold
        this.thresholdValue = thresholdValue
        this.creationDate = creationDate
        this.expirationDate = expirationDate
        this.availableDiscounts = availableDiscounts
        this.assignedDiscounts = assignedDiscounts
        this.redeemedDiscounts = redeemedDiscounts
    }
}