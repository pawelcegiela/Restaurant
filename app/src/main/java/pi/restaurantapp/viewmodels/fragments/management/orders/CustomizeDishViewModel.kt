package pi.restaurantapp.viewmodels.fragments.management.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.management.orders.CustomizeDishLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.dish.Dish
import pi.restaurantapp.objects.data.dish.DishBasic
import pi.restaurantapp.objects.data.dish.DishDetails
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.objects.enums.ToolbarType
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel
import java.math.BigDecimal

class CustomizeDishViewModel : AbstractPreviewItemViewModel() {
    override val logic = CustomizeDishLogic()

    private val _item: MutableLiveData<DishItem> = MutableLiveData()
    val item: LiveData<DishItem> = _item

    val observer = CustomizeDishObserver()

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<DishBasic>() ?: DishBasic()
        val details = snapshotsPair.details?.toObject<DishDetails>() ?: DishDetails()
        val dishItem = DishItem()
        dishItem.dish = Dish(itemId, basic, details)
        _item.value = dishItem

        initializeLists()
    }

    fun setItem(dishItem: DishItem) {
        _item.value = dishItem

        initializeLists()
    }

    fun initializeLists() {
        observer.baseIngredients = _item.value!!.dish.details.baseIngredients.toList().map { it.second }.toMutableList()
        observer.otherIngredients = _item.value!!.dish.details.otherIngredients.toList().map { it.second }.toMutableList()
        observer.possibleIngredients = _item.value!!.dish.details.possibleIngredients.toList().map { it.second }.toMutableList()
        observer.allergens = _item.value!!.dish.details.allergens.toList().map { it.second }.toMutableList()

        for (ingredient in _item.value!!.unusedOtherIngredients) {
            val ingredientItem = observer.otherIngredients.find { it.id == ingredient.id }
            observer.otherIngredients.remove(ingredientItem)
            ingredientItem?.let { observer.possibleIngredients.add(it) }
        }
        for (ingredient in _item.value!!.usedPossibleIngredients) {
            val ingredientItem = observer.possibleIngredients.find { it.id == ingredient.id }
            observer.possibleIngredients.remove(ingredientItem)
            ingredientItem?.let { observer.otherIngredients.add(it) }
        }
    }

    fun getFinalPrice(): String {
        var price = BigDecimal(if (item.value!!.dish.basic.isDiscounted) item.value!!.dish.basic.discountPrice else item.value!!.dish.basic.basePrice)
        price += observer.otherIngredients.sumOf { BigDecimal(it.extraPrice) }
        return (price * BigDecimal(item.value!!.amount)).toString()
    }

    override fun setToolbarType() {
        toolbarType.value = ToolbarType.ADD
    }

    override fun shouldGetDataFromDatabase(): Boolean {
        return _item.value == null
    }

    override fun isDisabled(): Boolean {
        return false
    }
}