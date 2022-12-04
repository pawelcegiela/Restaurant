package pi.restaurantapp.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.logic.fragments.AbstractItemListLogic
import pi.restaurantapp.objects.data.AbstractDataObject

abstract class AbstractItemListViewModel : AbstractFragmentViewModel() {
    private val _logic: AbstractItemListLogic get() = logic as AbstractItemListLogic

    private val _dataList: MutableLiveData<MutableList<AbstractDataObject>> = MutableLiveData<MutableList<AbstractDataObject>>()
    val dataList: LiveData<MutableList<AbstractDataObject>> = _dataList

    fun loadData() {
        _logic.loadData { list ->
            _dataList.value = list
        }
    }

    open fun displayFAB() = true

}