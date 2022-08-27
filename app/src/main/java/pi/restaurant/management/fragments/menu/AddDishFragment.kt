package pi.restaurant.management.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.data.Dish
import pi.restaurant.management.databinding.FragmentAddDishBinding

class AddDishFragment : Fragment() {

    private var _binding: FragmentAddDishBinding? = null
    val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO: Zablokowanie dla Workerów

        setSaveButtonListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDishBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setSaveButtonListener() {
        binding.buttonSaveData.setOnClickListener {
            //TODO zrobić na podstawie check required fields, porządnie
            if (
                binding.editTextName.text.trim().isEmpty() ||
                binding.editTextPrice.text.trim().isEmpty()
            ) {
                Toast.makeText(activity, "Fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val name = binding.editTextName.text.toString()
            val price = binding.editTextPrice.text.toString().toDouble()

            setValue(Dish(name, price))
        }
    }

    fun setValue(data: Dish) {
        val databaseRef = Firebase.database.getReference("menu").child(data.id)
        databaseRef.setValue(data)

        Toast.makeText(activity, getString(R.string.ingredient_added), Toast.LENGTH_SHORT)
            .show()

        findNavController().navigate(R.id.actionAddDishToMenu)
    }
}