package pi.restaurant.management.fragments.discounts

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
import pi.restaurant.management.databinding.FragmentAddDiscountBinding
import java.util.*
import kotlin.collections.ArrayList

class AddDiscountFragment : Fragment() {

    private var _binding: FragmentAddDiscountBinding? = null
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
        _binding = FragmentAddDiscountBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun initializeSpinner() {
        val spinner: Spinner = binding.spinnerType
        ArrayAdapter.createFromResource(
            context!!,
            R.array.types,
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
                binding.editTextAvailable.text.trim().isEmpty() ||
                binding.editTextAssigned.text.trim().isEmpty() ||
                binding.editTextUsed.text.trim().isEmpty() ||
                binding.editTextCode.text.trim().isEmpty() ||
                binding.editTextAmount.text.trim().isEmpty()
            ) {
                Toast.makeText(activity, "Fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val availableNumber = binding.editTextAvailable.text.toString().toInt()
            val assignedNumber = binding.editTextAssigned.text.toString().toInt()
            val usedNumber = binding.editTextUsed.text.toString().toInt()
            val code = binding.editTextCode.text.toString()
            val type = binding.spinnerType.selectedItemId.toInt()
            val amount = binding.editTextAmount.text.toString().toDouble()
            val date = Date() //TODO

            val available = createDiscounts(code, availableNumber, 0)
            val assigned = createDiscounts(code, assignedNumber, availableNumber)
            val used = createDiscounts(code, usedNumber, availableNumber + assignedNumber)

            setValue(DiscountGroup(available, assigned, used, code, type, amount, date))
        }
    }

    private fun createDiscounts(code: String, number: Int, startNumber: Int): ArrayList<String> {
        val discounts = ArrayList<String>()
        for (i in (startNumber + 1)..(startNumber + number)) {
            discounts.add("$code#$i")
        }
        return discounts
    }

    fun setValue(data: DiscountGroup) {
        val databaseRef = Firebase.database.getReference("discounts").child(data.code)
        databaseRef.setValue(data)

        Toast.makeText(activity, getString(R.string.discount_added), Toast.LENGTH_SHORT)
            .show()

        findNavController().navigate(R.id.actionAddDiscountToDiscounts)
    }
}