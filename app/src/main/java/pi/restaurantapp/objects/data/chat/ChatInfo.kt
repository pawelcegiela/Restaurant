package pi.restaurantapp.objects.data.chat

import pi.restaurantapp.objects.data.AbstractDataObject
import java.util.*

class ChatInfo : AbstractDataObject {
    var lastMessageDate: Long = Date().time
    var authorName: String = ""

    @Suppress("unused")
    constructor()

    constructor(authorId: String, lastMessageDate: Long, authorName: String) {
        this.id = authorId
        this.lastMessageDate = lastMessageDate
        this.authorName = authorName
    }
}