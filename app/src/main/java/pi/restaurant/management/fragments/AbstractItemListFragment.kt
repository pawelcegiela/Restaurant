package pi.restaurant.management.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.adapters.AbstractRecyclerAdapter
import pi.restaurant.management.adapters.WorkersRecyclerAdapter
import pi.restaurant.management.utils.SwipeCallback
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.databinding.FragmentItemListBinding

abstract class AbstractItemListFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null
    protected val binding get() = _binding!!

    abstract val databasePath: String
    abstract val addActionId: Int
    abstract val editActionId: Int
    var adapterData: MutableList<AbstractDataObject> = ArrayList()
    val progressBar get() = binding.progress.progressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFragment(databasePath, addActionId)
        addSwipeToEditCallback()
    }

    private fun initializeFragment(databasePath: String, actionId: Int) {
        val databaseRef = Firebase.database.getReference(databasePath)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                setData(dataSnapshot)
                progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        //TODO: Nie dla Workerów
        binding.fab.setOnClickListener {
            findNavController().navigate(actionId)
        }

        initializeSearchView()
    }

    private fun initializeSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                (binding.recyclerView.adapter as AbstractRecyclerAdapter?)?.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                (binding.recyclerView.adapter as AbstractRecyclerAdapter?)?.filter(newText)
                return false
            }
        })
    }

    abstract fun setData(dataSnapshot: DataSnapshot)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addSwipeToEditCallback() {
        if (arguments?.getBoolean("swipeEnabled", true) == false) {
            return
        }
        val swipeToEditCallback: SwipeCallback =
            object : SwipeCallback(
                activity!!.applicationContext,
                Color.YELLOW,
                com.google.android.material.R.drawable.material_ic_edit_black_24dp
            ) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                    checkPreconditionsAndOpenEdit(adapterData[viewHolder.adapterPosition])
                }
            }
        val itemTouchHelper = ItemTouchHelper(swipeToEditCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    open fun checkPreconditionsAndOpenEdit(item: AbstractDataObject) {
        openEdit(item)
    }

    fun openEdit(item: AbstractDataObject) {
        val bundle = Bundle()
        bundle.putString("id", item.id)

        findNavController().navigate(editActionId, bundle)
    }

    @Suppress("unused") // To be used in the future
    private fun removeItem(position: Int) {
        val dialogBuilder = AlertDialog.Builder(this.context)
        dialogBuilder.setTitle(getString(R.string.warning))
        dialogBuilder.setMessage(getString(R.string.do_you_want_to_remove))
        dialogBuilder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            removeFromDatabase(adapterData[position].id)
            adapterData.removeAt(position)
            binding.recyclerView.adapter?.notifyItemRemoved(position)
            Toast.makeText(context, getString(R.string.item_removed), Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialogBuilder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
        dialogBuilder.create().show()
    }

    private fun removeFromDatabase(itemId: String) {
        val databaseRef = Firebase.database.getReference(databasePath).child(itemId)

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    snapshot.ref.removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}