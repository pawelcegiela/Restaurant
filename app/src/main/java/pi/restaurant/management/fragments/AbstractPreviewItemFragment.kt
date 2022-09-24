package pi.restaurant.management.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import pi.restaurant.management.enums.Role

abstract class AbstractPreviewItemFragment : Fragment() {
    abstract val viewModel: AbstractPreviewItemViewModel

    abstract val linearLayout: LinearLayout
    abstract val editButton: Button?
    abstract val editActionId: Int
    lateinit var itemId: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemId = arguments?.getString("id").toString()
        viewModel.getUserRole()
        addLiveDataObservers()
    }

    private fun addLiveDataObservers() {
        viewModel.liveUserRole.observe(viewLifecycleOwner) { role ->
            if (role < Role.WORKER.ordinal) {
                initializeUI()
                viewModel.getDataFromDatabase(itemId)
            }
        }

        viewModel.liveDataSnapshot.observe(viewLifecycleOwner) { dataSnapshot ->
            fillInData(dataSnapshot)
        }
    }

    open fun initializeUI() {
        editButton?.visibility = View.VISIBLE
        editButton?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", itemId)

            findNavController().navigate(editActionId, bundle)
        }
    }

    abstract fun fillInData(dataSnapshot: DataSnapshot)
}