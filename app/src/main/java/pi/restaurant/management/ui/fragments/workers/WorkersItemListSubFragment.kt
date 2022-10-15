package pi.restaurant.management.ui.fragments.workers

import android.os.Bundle
import android.view.View
import pi.restaurant.management.objects.data.user.UserBasic
import pi.restaurant.management.objects.enums.DishType
import pi.restaurant.management.objects.enums.DishesTab
import pi.restaurant.management.objects.enums.Role
import pi.restaurant.management.objects.enums.WorkersTab
import pi.restaurant.management.ui.adapters.WorkersRecyclerAdapter
import pi.restaurant.management.ui.fragments.ItemListSubFragment

class WorkersItemListSubFragment(private var list: MutableList<UserBasic>, private val position: Int) : ItemListSubFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (position) {
            WorkersTab.ADMINS.ordinal -> list = list.filter { it.role == Role.ADMIN.ordinal }.toMutableList()
            WorkersTab.OWNERS.ordinal -> list = list.filter { it.role == Role.OWNER.ordinal }.toMutableList()
            WorkersTab.EXECUTIVES.ordinal -> list = list.filter { it.role == Role.EXECUTIVE.ordinal }.toMutableList()
            WorkersTab.MANAGERS.ordinal -> list = list.filter { it.role == Role.MANAGER.ordinal }.toMutableList()
            WorkersTab.WORKERS.ordinal -> list = list.filter { it.role == Role.WORKER.ordinal }.toMutableList()
        }
        binding.recyclerView.adapter = WorkersRecyclerAdapter(list, this)
    }
}