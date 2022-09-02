package pi.restaurant.management.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.Dish
import pi.restaurant.management.databinding.FragmentPreviewDishBinding
import pi.restaurant.management.fragments.AbstractPreviewItemFragment

class PreviewDishFragment : AbstractPreviewItemFragment() {
    override val databasePath = "menu"
    override val linearLayout get() = binding.linearLayout

    private var _binding: FragmentPreviewDishBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewDishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun fillInData(dataSnapshot: DataSnapshot) {
        val item = dataSnapshot.getValue<Dish>() ?: return
        binding.textViewName.text = item.name
        binding.textViewPrice.text = "${item.price} zł"
        binding.progress.progressBar.visibility = View.GONE
    }
}