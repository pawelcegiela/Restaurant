package pi.restaurantapp.objects.data.notification

import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.AbstractDataObject
import java.util.*

/**
 * Data class containing information of Notification for notification center.
 */
class Notification : AbstractDataObject {
    var date: Date = Date()
    var text: String = ""
    var targetGroup: Int = -1
    var targetUserId: String = ""
    var disabled: Boolean = false

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        date: Date,
        text: String,
        targetGroup: Int,
        targetUserId: String,
    ) {
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.date = date
        this.text = text
        this.targetGroup = targetGroup
        this.targetUserId = targetUserId
    }
}