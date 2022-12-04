package pi.restaurantapp.logic.fragments.management.chats

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.fragments.AbstractItemListLogic
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.chat.ChatInfo

class ChatsChooseCustomerLogic : AbstractItemListLogic() {
    override val databasePath = "chats"
    override val dbRef get() = Firebase.firestore.collection("$databasePath-basic").orderBy("lastMessageDate", Query.Direction.DESCENDING)

    override fun retrieveDataList(snapshot: QuerySnapshot, callback: (MutableList<AbstractDataObject>) -> Unit) {
        val dataList = snapshot.map { document -> document.toObject<ChatInfo>() }.toMutableList<AbstractDataObject>()
        callback(dataList)
    }
}