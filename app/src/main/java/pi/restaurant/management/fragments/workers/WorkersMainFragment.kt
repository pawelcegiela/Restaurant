package pi.restaurant.management.fragments.workers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.adapters.WorkersRecyclerAdapter
import pi.restaurant.management.data.UserData
import pi.restaurant.management.databinding.FragmentWorkersMainBinding
import pi.restaurant.management.fragments.SplashScreenFragment
import pi.restaurant.management.utils.Role

class WorkersMainFragment : SplashScreenFragment() {
    private var _binding: FragmentWorkersMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkersMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val databaseRef = Firebase.database.getReference("users")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<HashMap<String, UserData>>() ?: return
                val list = data.toList().map { it.second }
                val userId = data[Firebase.auth.currentUser?.uid]?.id as String
                val userRole = data[Firebase.auth.currentUser?.uid]?.role as Int
                binding.recyclerView.adapter =
                    WorkersRecyclerAdapter(list, this@WorkersMainFragment, userId, userRole)
                if (userRole == Role.WORKER.ordinal) {
                    binding.fabNewWorker.visibility = View.GONE
                }
                keepSplashScreen = false
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        //TODO: Nie dla Workerów
        binding.fabNewWorker.setOnClickListener {
            findNavController().navigate(R.id.actionWorkersToAddWorker)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}