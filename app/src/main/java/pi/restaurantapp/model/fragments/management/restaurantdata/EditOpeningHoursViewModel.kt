package pi.restaurantapp.model.fragments.management.restaurantdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ktx.getValue
import pi.restaurantapp.model.fragments.management.AbstractModifyItemViewModel
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.openinghours.OpeningHours
import pi.restaurantapp.objects.data.openinghours.OpeningHoursBasic
import pi.restaurantapp.objects.data.openinghours.OpeningHoursDetails

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