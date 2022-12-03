package pi.restaurantapp.ui.fragments.management.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pi.restaurantapp.viewmodels.activities.management.ChatsViewModel
import pi.restaurantapp.objects.data.chat.ChatInfo
import pi.restaurantapp.ui.adapters.ChatInfoRecyclerAdapter
import pi.restaurantapp.ui.fragments.ItemListSubFragment

class ChatsItemListSubFragment(private val list: MutableList<ChatInfo>, fabFilter: FloatingActionButton, searchView: SearchView) :
    ItemListSubFragment(fabFilter, searchView) {
    private val _activityViewModel: ChatsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        activityViewModel = _activityViewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
    }

    private fun getFilteredList(): MutableList<ChatInfo> {
        return list
    }

    override fun setAdapter() {
        binding.recyclerView.adapter = ChatInfoRecyclerAdapter(getFilteredList(), this)
    }
}