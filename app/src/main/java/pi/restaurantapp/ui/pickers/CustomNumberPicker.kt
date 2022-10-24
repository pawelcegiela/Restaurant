package pi.restaurantapp.ui.pickers

import pi.restaurantapp.databinding.CustomNumberPickerBinding

class CustomNumberPicker(
    private val numberPicker: CustomNumberPickerBinding,
    private val minimumValue: Int,
    private val maximumValue: Int,
    private val step: Int,
    val notifyFragment: () -> (Unit)
) {

    init {
        setButtonListeners()
    }

    private fun setButtonListeners() {
        numberPicker.buttonLess.setOnClickListener {
            if (getValue() - step >= minimumValue) {
                setValue(getValue() - step)
                notifyFragment()
            }
        }

        numberPicker.buttonMore.setOnClickListener {
            if (getValue() + step <= maximumValue) {
                setValue(getValue() + step)
                notifyFragment()
            }
        }
    }

    fun setValue(value: Int) {
        numberPicker.textViewNumber.text = value.toString()
    }

    fun getValue(): Int {
        return numberPicker.textViewNumber.text.toString().toInt()
    }
}