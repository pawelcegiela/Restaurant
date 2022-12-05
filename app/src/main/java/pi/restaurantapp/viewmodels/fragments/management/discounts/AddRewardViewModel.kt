package pi.restaurantapp.viewmodels.fragments.management.discounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.R
import pi.restaurantapp.logic.fragments.management.discounts.AddRewardLogic
import pi.restaurantapp.viewmodels.fragments.AbstractFragmentViewModel

class AddRewardViewModel : AbstractFragmentViewModel() {
    override val logic = AddRewardLogic()

    val totalValue: MutableLiveData<String> = MutableLiveData()
    val minimumValue: MutableLiveData<String> = MutableLiveData()
    val days: MutableLiveData<String> = MutableLiveData()

    private val _toastMessage: MutableLiveData<Int> = MutableLiveData<Int>()
    val toastMessage: LiveData<Int> = _toastMessage

    fun generateDiscounts() {
        if (totalValue.value?.toLong() == null || minimumValue.value?.toLong() == null || days.value?.toLong() == null) {
            _toastMessage.value = R.string.enter_reward_values
            return
        }
        val totalValueL = totalValue.value?.toLong() ?: return
        val minimumValueL = minimumValue.value?.toLong() ?: return
        val daysL = days.value?.toLong() ?: return

        if (totalValueL < 5 || minimumValueL < 1 || daysL < 7) {
            _toastMessage.value = R.string.reward_values_should_be
            return
        }

        logic.generateDiscountRewards(totalValueL, minimumValueL, daysL)
    }
}