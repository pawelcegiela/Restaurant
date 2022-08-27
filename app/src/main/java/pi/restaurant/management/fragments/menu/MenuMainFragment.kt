package pi.restaurant.management.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.adapters.MenuRecyclerAdapter
import pi.restaurant.management.data.Dish
import pi.restaurant.management.databinding.FragmentMenuMainBinding
import pi.restaurant.management.fragments.SplashScreenFragment

class MenuMainFragment : SplashScreenFragment() {
    private var _binding: FragmentMenuMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val databaseRef = Firebase.database.getReference("menu")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val data = dataSnapshot.getValue<HashMap<String, Dish>>() ?: return
                    val list = data.toList().map { it.second }
                    binding.recyclerViewMenu.adapter =
                        MenuRecyclerAdapter(list, this@MenuMainFragment)
                    keepSplashScreen = false
                    //TODO Dokończyć
                } catch (ex: Exception) {

                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        keepSplashScreen = false

        //TODO: Nie dla Workerów
        binding.fabNewDish.setOnClickListener {
            findNavController().navigate(R.id.actionMenuToAddDish)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}