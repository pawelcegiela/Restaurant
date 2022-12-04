package pi.restaurantapp.viewmodels.fragments.management.restaurantdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.management.restaurantdata.EditOpeningHoursLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.openinghours.OpeningHours
import pi.restaurantapp.objects.data.openinghours.OpeningHoursBasic
import pi.restaurantapp.objects.data.openinghours.OpeningHoursDetails
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel

class EditOpeningHoursViewModel : AbstractModifyItemViewModel() {
    override val logic = EditOpeningHoursLogic()

    private val _item: MutableLiveData<OpeningHours> = MutableLiveData()
    val item: LiveData<OpeningHours> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<OpeningHoursBasic>() ?: OpeningHoursBasic()
        val details = snapshotsPair.details?.toObject<OpeningHoursDetails>() ?: OpeningHoursDetails()
        _item.value = OpeningHours(basic, details)
    }
}