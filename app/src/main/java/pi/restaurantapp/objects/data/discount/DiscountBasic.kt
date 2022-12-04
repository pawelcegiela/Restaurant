package pi.restaurantapp.objects.data.discount

import pi.restaurantapp.logic.utils.ComputingUtils
import pi.restaurantapp.objects.data.AbstractDataObject
import java.util.*

class DiscountBasic : AbstractDataObject {
    var amount: String = "0.0"
    var valueType: Int = 0
    var hasThreshold: Boolean = false
    var thresholdValue: String = "0.0"
    var creationDate: Date = Date()
    var expirationDate: Date = ComputingUtils.getInitialExpirationDate()
    var numberOfDiscounts: Int = 0
    var assignedDiscounts: ArrayList<String> = ArrayList()
    var redeemedDiscounts: ArrayList<String> = ArrayList()
    var receiverType: Int = 0
    var usageType: Int = 0
    var disabled: Boolean = false

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        amount: String,
        valueType: Int,
        hasThreshold: Boolean,
        thresholdValue: String,
        creationDate: Date,
        expirationDate: Date,
        numberOfDiscounts: Int,
        assignedDiscounts: ArrayList<String>,
        redeemedDiscounts: ArrayList<String>,
        receiverType: Int,
        usageType: Int
    ) {
        this.id = id
        this.amount = amount
        this.valueType = valueType
        this.hasThreshold = hasThreshold
        this.thresholdValue = thresholdValue
        this.creationDate = creationDate
        this.expirationDate = expirationDate
        this.numberOfDiscounts = numberOfDiscounts
        this.assignedDiscounts = assignedDiscounts
        this.redeemedDiscounts = redeemedDiscounts
        this.receiverType = receiverType
        this.usageType = usageType
    }

    fun onNumberOfDiscountsChanged(s: CharSequence) {
        try {
            this.numberOfDiscounts = s.toString().toInt()
        } catch (_: NumberFormatException) {
        }
    }

    fun onAmountChanged(s: CharSequence) {
        try {
            this.amount = s.toString().toDouble().toString()
        } catch (_: NumberFormatException) {
        }
    }

    fun onExpirationDateChanged(s: CharSequence) {
        this.expirationDate = ComputingUtils.getDateTimeFromString(s.toString())
    }
}