package pi.restaurantapp.model.fragments.management.workers

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.data.user.User
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.data.user.UserDetails

class AddWorkerViewModel : AbstractModifyWorkerViewModel() {
    override fun createItem() {
        setItem(User("", UserBasic(), UserDetails()))
        setReadyToInitialize()
    }

    fun setNewUserId() {
        itemId = Firebase.auth.uid!!
        item.value!!.id = itemId
        item.value!!.basic.id = itemId
        item.value!!.details.id = itemId
    }
}