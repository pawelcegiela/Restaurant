package pi.restaurantapp.viewmodels.fragments.management.discounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.objects.data.NullableSplitDataObject
import pi.restaurantapp.objects.data.discount.Discount
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.enums.Precondition
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel

/**
 * Abstract class responsible for presentation logic and binding between data/model and view (ViewModel layer) for AbstractModifyDiscountFragment.
 * @see pi.restaurantapp.logic.fragments.management.discounts.AbstractModifyDiscountLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.discounts.AbstractModifyDiscountFragment View layer
 */
abstract class AbstractModifyDiscountViewModel : AbstractModifyItemViewModel() {
    private val _item: MutableLiveData<Discount> = MutableLiveData()
    val item: LiveData<Discount> = _item

    override val splitDataObject get() = NullableSplitDataObject(itemId, item.value?.basic, item.value?.details)

    private val _list = MutableLiveData<MutableList<DiscountBasic>>()
    val list: LiveData<MutableList<DiscountBasic>> = _list

    fun setItem(newItem: Discount) {
        _item.value = newItem
    }

    fun setList(list: MutableList<DiscountBasic>) {
        _list.value = list
    }

    override fun checkSavePreconditions(): Precondition {
        if (super.checkSavePreconditions() != Precondition.OK) {
            return super.checkSavePreconditions()
        }
        val discount = _item.value!!.basic
        if (list.value?.any { it.id == discount.id && it.creationDate != discount.creationDate } == true) {
            return Precondition.DISCOUNT_CODE_EXISTS
        }
        return Precondition.OK
    }
}