package pi.restaurant.management.ui.views

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private val minimumPeriod: Long = 1000 * 60 * 30 // 30 minutes
    private val defaultPeriod: Long = 1000 * 60 * 45 // 45 minutes

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        c.timeInMillis += defaultPeriod
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val c = Calendar.getInstance()
        c.timeInMillis += minimumPeriod
        val minimumHour = c.get(Calendar.HOUR_OF_DAY)
        val minimumMinute = c.get(Calendar.MINUTE)
        if (hourOfDay < minimumHour || (hourOfDay == minimumHour && minute < minimumMinute)) {
            Toast.makeText(requireContext(), "zle", Toast.LENGTH_SHORT).show()
        }
    }
}
