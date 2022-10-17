package pi.restaurant.management.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pi.restaurant.management.databinding.FragmentItemListSubBinding
import pi.restaurant.management.model.activities.AbstractActivityViewModel
import pi.restaurant.management.ui.adapters.AbstractRecyclerAdapter
import pi.restaurant.management.ui.views.DialogFilter

open class ItemListSubFragment(private val fabFilter: FloatingActionButton) : Fragment() {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun onPause() {
        super.onPause()
        fabFilter.setOnClickListener(null)
    }

    override fun onResume() {
        super.onResume()
        setAdapter()

        fabFilter.setOnClickListener {
            DialogFilter(this, activityViewModel.getShowActive(), activityViewModel.getShowDisabled()) { active, disabled ->
                activityViewModel.setShowActive(active)
                activityViewModel.setShowDisabled(disabled)
                setAdapter()
            }
        }
    }

    open fun setAdapter() {}
}