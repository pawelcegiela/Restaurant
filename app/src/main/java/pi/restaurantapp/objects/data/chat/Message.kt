package pi.restaurantapp.objects.data.chat

import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.AbstractDataObject
import java.util.*

/**
 * Data class containing chat message.
 */
class Message : AbstractDataObject {
    var message: String = ""
    var timestamp: Long = Date().time
    var authorId: String = ""
    var authorName: String = ""

    @Suppress("unused")
    constructor()

    constructor(message: String, authorId: String, authorName: String) {
        this.id = StringFormatUtils.formatId()
        this.message = message
        this.authorId = authorId
        this.authorName = authorName
    }
}