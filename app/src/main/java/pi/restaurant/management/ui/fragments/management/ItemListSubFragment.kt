package pi.restaurant.management.ui.fragments.management

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pi.restaurant.management.databinding.FragmentItemListSubBinding
import pi.restaurant.management.model.activities.management.AbstractActivityViewModel
import pi.restaurant.management.ui.adapters.AbstractRecyclerAdapter
import pi.restaurant.management.ui.dialogs.FilterDialog

open class ItemListSubFragment(private val fabFilter: FloatingActionButton, private val searchView: SearchView) : Fragment() {
    private var _binding: FragmentItemListSubBinding? = null
    protected val binding get() = _binding!!
    protected lateinit var activityViewModel: AbstractActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemListSubBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setAdapter()
        initializeSearchView()

        fabFilter.setOnClickListener {
            FilterDialog(this, activityViewModel.getShowActive(), activityViewModel.getShowDisabled()) { active, disabled ->
                activityViewModel.setShowActive(active)
                activityViewModel.setShowDisabled(disabled)
                setAdapter()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        fabFilter.setOnClickListener(null)
        searchView.setOnQueryTextListener(null)
    }

    private fun initializeSearchView() {
        (binding.recyclerView.adapter as AbstractRecyclerAdapter?)?.filter(searchView.query.toString())
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

    open fun setAdapter() {}
}