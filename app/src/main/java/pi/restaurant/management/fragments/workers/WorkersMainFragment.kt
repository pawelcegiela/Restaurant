package pi.restaurant.management.fragments.workers

import android.view.View
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.adapters.WorkersRecyclerAdapter
import pi.restaurant.management.data.UserData
import pi.restaurant.management.enums.Role
import pi.restaurant.management.fragments.AbstractItemListFragment

class WorkersMainFragment : AbstractItemListFragment() {
    override val databasePath = "users"
    override val fabActionId = R.id.actionWorkersToAddWorker

    override fun setData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, UserData>>() ?: return
        val list = data.toList().map { it.second }
        val userId = data[Firebase.auth.currentUser?.uid]?.id as String
        val userRole = data[Firebase.auth.currentUser?.uid]?.role as Int
        binding.recyclerView.adapter =
            WorkersRecyclerAdapter(list, this@WorkersMainFragment, userId, userRole)
        if (userRole == Role.WORKER.ordinal) {
            binding.fab.visibility = View.GONE
        }
    }
}