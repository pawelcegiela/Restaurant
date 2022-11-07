package pi.restaurantapp.model.fragments.management.chats

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.model.fragments.management.AbstractItemListViewModel
import pi.restaurantapp.objects.data.chat.ChatInfo

class ChatsChooseCustomerViewModel : AbstractItemListViewModel() {
    override val databasePath = "chats"

    override fun loadData() {
        val databaseRef = Firebase.database.getReference(databasePath).child("basic")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                retrieveDataList(dataSnapshot)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun retrieveDataList(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, ChatInfo>>() ?: HashMap()
        setDataList(data.toList().map { it.second }.sortedByDescending { it.lastMessageDate }.toMutableList())
    }

    override fun displayFAB() = false
}