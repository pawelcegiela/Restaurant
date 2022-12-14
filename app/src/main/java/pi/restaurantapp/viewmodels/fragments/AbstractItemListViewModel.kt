package pi.restaurantapp.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.logic.fragments.AbstractItemListLogic
import pi.restaurantapp.objects.data.AbstractDataObject

/**
 * Abstract class responsible for presentation logic and binding between data/model and view (ViewModel layer) for AbstractItemListFragment.
 * @see pi.restaurantapp.logic.fragments.AbstractItemListLogic Model layer
 * @see pi.restaurantapp.ui.fragments.AbstractItemListFragment View layer
 */
abstract class AbstractItemListViewModel : AbstractFragmentViewModel() {
    private val _logic: AbstractItemListLogic get() = logic as AbstractItemListLogic

    private val _dataList: MutableLiveData<MutableList<AbstractDataObject>> = MutableLiveData<MutableList<AbstractDataObject>>()
    val dataList: LiveData<MutableList<AbstractDataObject>> = _dataList

    open fun loadData() {
        _logic.loadData { list ->
            _dataList.value = list
        }
    }

    open fun displayFAB() = true

    fun setDataList(list: MutableList<AbstractDataObject>) {
        _dataList.value = list
    }

}