package pi.restaurant.management.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.databinding.FragmentRecyclerBinding

abstract class AbstractItemListFragment : AbstractSplashScreenFragment() {

    private var _binding: FragmentRecyclerBinding? = null
    protected val binding get() = _binding!!

    abstract val databasePath: String
    abstract val fabActionId: Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFragment(databasePath, fabActionId)
    }

    private fun initializeFragment(databasePath: String, actionId: Int) {
        val databaseRef = Firebase.database.getReference(databasePath)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                setData(dataSnapshot)
                keepSplashScreen = false
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        //TODO: Nie dla Workerów
        binding.fab.setOnClickListener {
            findNavController().navigate(actionId)
        }
    }

    abstract fun setData(dataSnapshot: DataSnapshot)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}