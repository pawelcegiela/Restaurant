package pi.restaurant.management.fragments.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.data.DiscountGroup
import pi.restaurant.management.data.Ingredient
import pi.restaurant.management.databinding.FragmentAddIngredientBinding
import java.util.*
import kotlin.collections.ArrayList

class AddIngredientFragment : Fragment() {

    private var _binding: FragmentAddIngredientBinding? = null
    val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO: Zablokowanie dla Workerów

        initializeSpinner()
        setSaveButtonListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddIngredientBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun initializeSpinner() {
        val spinner: Spinner = binding.spinnerUnit
        ArrayAdapter.createFromResource(
            context!!,
            R.array.units,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    private fun setSaveButtonListener() {
        binding.buttonSaveData.setOnClickListener {
            //TODO zrobić na podstawie check required fields, porządnie
            if (
                binding.editTextName.text.trim().isEmpty() ||
                binding.editTextAmount.text.trim().isEmpty()
            ) {
                Toast.makeText(activity, "Fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val name = binding.editTextName.text.toString()
            val amount = binding.editTextAmount.text.toString().toInt()

            setValue(Ingredient(name, amount, binding.spinnerUnit.selectedItemPosition))
        }
    }

    fun setValue(data: Ingredient) {
        val databaseRef = Firebase.database.getReference("ingredients").child(data.id)
        databaseRef.setValue(data)

        Toast.makeText(activity, getString(R.string.ingredient_added), Toast.LENGTH_SHORT)
            .show()

        findNavController().navigate(R.id.actionAddIngredientToIngredients)
    }
}