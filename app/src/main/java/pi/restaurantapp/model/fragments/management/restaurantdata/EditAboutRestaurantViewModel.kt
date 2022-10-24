package pi.restaurantapp.model.fragments.management.restaurantdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ktx.getValue
import pi.restaurantapp.model.fragments.management.AbstractModifyItemViewModel
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.aboutrestaurant.AboutRestaurant
import pi.restaurantapp.objects.data.aboutrestaurant.AboutRestaurantBasic
import pi.restaurantapp.objects.data.aboutrestaurant.AboutRestaurantDetails

class EditAboutRestaurantViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "restaurantData"

    private val _item: MutableLiveData<AboutRestaurant> = MutableLiveData()
    val item: LiveData<AboutRestaurant> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.getValue<AboutRestaurantBasic>() ?: AboutRestaurantBasic()
        val details = snapshotsPair.details?.getValue<AboutRestaurantDetails>() ?: AboutRestaurantDetails()
        _item.value = AboutRestaurant(basic, details)
    }
}