package pi.restaurant.management.ui.views

import pi.restaurant.management.databinding.CustomNumberPickerBinding

class CustomNumberPicker(private val numberPicker: CustomNumberPickerBinding, val notifyFragment: () -> (Unit)) {
    private val minimumValue = 1
    private val maximumValue = 10

    init {
        setButtonListeners()
    }

    private fun setButtonListeners() {
        numberPicker.buttonLess.setOnClickListener {
            if (getValue() > minimumValue) {
                setValue(getValue() - 1)
                notifyFragment()
            }
        }

        numberPicker.buttonMore.setOnClickListener {
            if (getValue() < maximumValue) {
                setValue(getValue() + 1)
                notifyFragment()
            }
        }
    }

    fun setValue(value: Int) {
        numberPicker.textViewNumber.text = value.toString()
    }

    fun getValue() : Int {
        return numberPicker.textViewNumber.text.toString().toInt()
    }
}