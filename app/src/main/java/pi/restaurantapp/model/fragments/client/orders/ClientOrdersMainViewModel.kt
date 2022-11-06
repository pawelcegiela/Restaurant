package pi.restaurantapp.model.fragments.client.orders

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.model.fragments.management.AbstractItemListViewModel
import pi.restaurantapp.objects.data.order.OrderBasic

class ClientOrdersMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "orders"

    override fun loadData() {
        val databaseRef = Firebase.database.getReference(databasePath).child("basic").orderByChild("userId").equalTo(Firebase.auth.uid)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                retrieveDataList(dataSnapshot)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun retrieveDataList(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, OrderBasic>>() ?: HashMap()
        setDataList(data.toList().map { it.second }.toMutableList().sortedByDescending { it.id }.toMutableList())
    }
}