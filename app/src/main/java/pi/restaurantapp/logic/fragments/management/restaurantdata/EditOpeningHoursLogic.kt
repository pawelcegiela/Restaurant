package pi.restaurantapp.logic.fragments.management.restaurantdata

import pi.restaurantapp.logic.fragments.AbstractModifyItemLogic
import pi.restaurantapp.objects.data.openinghours.OpeningHoursBasic

/**
 * Class responsible for business logic and communication with database (Model layer) for EditOpeningHoursFragment.
 * @see pi.restaurantapp.ui.fragments.management.restaurantdata.EditOpeningHoursFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.restaurantdata.EditOpeningHoursViewModel ViewModel layer
 */
class EditOpeningHoursLogic : AbstractModifyItemLogic() {
    override val databasePath = "restaurantData"

    fun checkValues(basic: OpeningHoursBasic): Boolean {
        for (i in basic.enabledList.indices) {
            if (basic.startHoursList[i] >= basic.endHoursList[i]) {
                return true
            }
        }
        return (basic.defaultStartHour >= basic.defaultEndHour)
    }
}