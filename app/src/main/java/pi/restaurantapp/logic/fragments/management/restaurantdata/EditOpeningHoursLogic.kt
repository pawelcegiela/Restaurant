package pi.restaurantapp.logic.fragments.management.restaurantdata

import pi.restaurantapp.logic.fragments.AbstractModifyItemLogic
import pi.restaurantapp.objects.data.openinghours.OpeningHoursBasic

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