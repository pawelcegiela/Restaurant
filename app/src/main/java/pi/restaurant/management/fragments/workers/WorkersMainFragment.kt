package pi.restaurant.management.fragments.workers

import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.adapters.WorkersRecyclerAdapter
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.User
import pi.restaurant.management.enums.Role
import pi.restaurant.management.fragments.AbstractItemListFragment

class WorkersMainFragment : AbstractItemListFragment() {
    override val databasePath = "users"
    override val addActionId = R.id.actionWorkersToAddWorker
    override val editActionId = R.id.actionWorkersToEditWorker
    private var userRole = Role.WORKER.ordinal

    override fun setData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, User>>() ?: return
        val list = data.toList().map { it.second }.toMutableList()
        userRole = data[Firebase.auth.currentUser?.uid]?.role as Int
        binding.recyclerView.adapter = WorkersRecyclerAdapter(list, this@WorkersMainFragment)
        if (userRole == Role.WORKER.ordinal) {
            binding.fab.visibility = View.GONE
        }
        adapterData = list as MutableList<AbstractDataObject>
    }

    override fun checkPreconditionsAndOpenEdit(item: AbstractDataObject) {
        val user = item as User
        if (user.role > userRole) {
            openEdit(user)
        } else if (user.id == Firebase.auth.uid) {
            findNavController().navigate(R.id.actionWorkersToEditMyData)
        } else {
            Toast.makeText(
                context,
                context?.getString(R.string.no_permission_user_data) ?: "",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}