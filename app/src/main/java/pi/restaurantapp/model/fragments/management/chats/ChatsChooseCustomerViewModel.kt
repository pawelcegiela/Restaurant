package pi.restaurantapp.model.fragments.management.chats

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.model.fragments.AbstractItemListViewModel
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.chat.ChatInfo

class ChatsChooseCustomerViewModel : AbstractItemListViewModel() {
    override val databasePath = "chats"
    override val dbRef get() = Firebase.firestore.collection("$databasePath-basic").orderBy("lastMessageDate", Query.Direction.DESCENDING)

    override fun retrieveDataList(snapshot: QuerySnapshot) {
        val dataList = snapshot.map { document -> document.toObject<ChatInfo>() }.toMutableList<AbstractDataObject>()
        setDataList(dataList)
    }

    override fun displayFAB() = false
}