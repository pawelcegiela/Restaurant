package pi.restaurantapp.viewmodels.fragments.management.restaurantdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.aboutrestaurant.AboutRestaurant
import pi.restaurantapp.objects.data.aboutrestaurant.AboutRestaurantBasic
import pi.restaurantapp.objects.data.aboutrestaurant.AboutRestaurantDetails

class EditAboutRestaurantViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "restaurantData"

    private val _item: MutableLiveData<AboutRestaurant> = MutableLiveData()
    val item: LiveData<AboutRestaurant> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<AboutRestaurantBasic>() ?: AboutRestaurantBasic()
        val details = snapshotsPair.details?.toObject<AboutRestaurantDetails>() ?: AboutRestaurantDetails()
        _item.value = AboutRestaurant(basic, details)
    }
}