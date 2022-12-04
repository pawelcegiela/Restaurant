package pi.restaurantapp.viewmodels.fragments.client.discounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.fragments.client.discounts.ClientDiscountsMainLogic
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

class ClientDiscountsMainViewModel : AbstractItemListViewModel() {
    override val logic = ClientDiscountsMainLogic()

    private val _newDiscountMessage: MutableLiveData<Int> = MutableLiveData<Int>()
    val newDiscountMessage: LiveData<Int> = _newDiscountMessage

    private val _newDiscount: MutableLiveData<DiscountBasic> = MutableLiveData<DiscountBasic>()
    val newDiscount: LiveData<DiscountBasic> = _newDiscount

    fun addNewDiscount(code: String) {
        logic.addNewDiscount(
            code,
            { messageId ->
                _newDiscountMessage.value = messageId
            }, { discount ->
                discount.assignedDiscounts.add(Firebase.auth.uid!!)
                _newDiscount.value = discount
            }
        )
    }
}