package pi.restaurantapp.viewmodels.fragments.management.restaurantdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.management.restaurantdata.EditAboutRestaurantLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.NullableSplitDataObject
import pi.restaurantapp.objects.data.aboutrestaurant.AboutRestaurant
import pi.restaurantapp.objects.data.aboutrestaurant.AboutRestaurantBasic
import pi.restaurantapp.objects.data.aboutrestaurant.AboutRestaurantDetails
import pi.restaurantapp.objects.enums.ToolbarType
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel

class EditAboutRestaurantViewModel : AbstractModifyItemViewModel() {
    override val logic = EditAboutRestaurantLogic()

    private val _item: MutableLiveData<AboutRestaurant> = MutableLiveData()
    val item: LiveData<AboutRestaurant> = _item

    override val splitDataObject get() = NullableSplitDataObject(itemId, item.value?.basic, item.value?.details)

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<AboutRestaurantBasic>() ?: AboutRestaurantBasic()
        val details = snapshotsPair.details?.toObject<AboutRestaurantDetails>() ?: AboutRestaurantDetails()
        _item.value = AboutRestaurant(basic, details)

        toolbarType.value = ToolbarType.SAVE
    }
}