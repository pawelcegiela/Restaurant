package pi.restaurantapp.viewmodels.fragments.management.restaurantdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.management.restaurantdata.EditOpeningHoursLogic
import pi.restaurantapp.logic.utils.PreconditionUtils
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.NullableSplitDataObject
import pi.restaurantapp.objects.data.openinghours.OpeningHours
import pi.restaurantapp.objects.data.openinghours.OpeningHoursBasic
import pi.restaurantapp.objects.data.openinghours.OpeningHoursDetails
import pi.restaurantapp.objects.enums.Precondition
import pi.restaurantapp.objects.enums.ToolbarType
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for EditOpeningHoursFragment.
 * @see pi.restaurantapp.logic.fragments.management.restaurantdata.EditOpeningHoursLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.restaurantdata.EditOpeningHoursFragment View layer
 */
class EditOpeningHoursViewModel : AbstractModifyItemViewModel() {
    override val logic = EditOpeningHoursLogic()

    private val _item: MutableLiveData<OpeningHours> = MutableLiveData()
    val item: LiveData<OpeningHours> = _item

    override val splitDataObject get() = NullableSplitDataObject(itemId, item.value?.basic, item.value?.details)

    private val sdf = SimpleDateFormat("HH:mm", Locale.ROOT)

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<OpeningHoursBasic>() ?: OpeningHoursBasic()
        val details = snapshotsPair.details?.toObject<OpeningHoursDetails>() ?: OpeningHoursDetails()
        _item.value = OpeningHours(basic, details)

        toolbarType.value = ToolbarType.SAVE
    }

    override fun checkSavePreconditions(): Precondition {
        if (super.checkSavePreconditions() != Precondition.OK) {
            return super.checkSavePreconditions()
        }
        return PreconditionUtils.checkOpeningHoursError(item.value!!.basic)
    }

    fun onEnabledChanged(enabled: Boolean, number: Int) {
        item.value ?: return
        item.value!!.basic.enabledList[number] = enabled
        item.value!!.basic.isError = logic.checkValues(item.value!!.basic)
    }

    fun onTextChangedStartHour(text: CharSequence?, number: Int) {
        if (item.value == null || text == null || text.isEmpty()) {
            return
        }

        if (number < 0) {
            item.value!!.basic.defaultStartHour = sdf.parse(text.toString()) as Date
        } else {
            item.value!!.basic.startHoursList[number] = sdf.parse(text.toString()) as Date
        }

        item.value!!.basic.isError = logic.checkValues(item.value!!.basic)
    }

    fun onTextChangedEndHour(text: CharSequence?, number: Int) {
        if (item.value == null || text == null || text.isEmpty()) {
            return
        }

        if (number < 0) {
            item.value!!.basic.defaultEndHour = sdf.parse(text.toString()) as Date
        } else {
            item.value!!.basic.endHoursList[number] = sdf.parse(text.toString()) as Date
        }

        item.value!!.basic.isError = logic.checkValues(item.value!!.basic)
    }
}