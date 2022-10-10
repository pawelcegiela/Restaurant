package pi.restaurant.management.model.fragments.restaurantdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.model.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.objects.SnapshotsPair
import pi.restaurant.management.objects.data.openinghours.OpeningHours
import pi.restaurant.management.objects.data.openinghours.OpeningHoursBasic
import pi.restaurant.management.objects.data.openinghours.OpeningHoursDetails

class EditOpeningHoursViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "restaurantData"

    private val _item: MutableLiveData<OpeningHours> = MutableLiveData()
    val item: LiveData<OpeningHours> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.getValue<OpeningHoursBasic>() ?: OpeningHoursBasic()
        val details = snapshotsPair.details?.getValue<OpeningHoursDetails>() ?: OpeningHoursDetails()
        _item.value = OpeningHours(basic, details)
    }
}