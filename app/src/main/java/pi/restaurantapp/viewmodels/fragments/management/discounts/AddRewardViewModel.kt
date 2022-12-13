package pi.restaurantapp.viewmodels.fragments.management.discounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.R
import pi.restaurantapp.logic.fragments.management.discounts.AddRewardLogic
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.viewmodels.fragments.AbstractFragmentViewModel

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for AddRewardFragment.
 * @see pi.restaurantapp.logic.fragments.management.discounts.AddRewardLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.discounts.AddRewardFragment View layer
 */
class AddRewardViewModel : AbstractFragmentViewModel() {
    override val logic = AddRewardLogic()

    val totalValue: MutableLiveData<String> = MutableLiveData()
    val minimumValue: MutableLiveData<String> = MutableLiveData()
    val days: MutableLiveData<String> = MutableLiveData()

    private val _toastMessage: MutableLiveData<Int> = MutableLiveData<Int>()
    val toastMessage: LiveData<Int> = _toastMessage

    private val _rewardsToDisplay = MutableLiveData<String>()
    val rewardsToDisplay: LiveData<String> = _rewardsToDisplay

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

        logic.generateDiscountRewards(totalValueL, minimumValueL, daysL) { success, rewards ->
            if (success) {
                _toastMessage.value = R.string.rewards_have_been_generated
                _rewardsToDisplay.value = StringFormatUtils.formatRewardsToDisplay(rewards)
            } else {
                _toastMessage.value = R.string.rewards_couldnt_be_generated
            }
        }
    }
}