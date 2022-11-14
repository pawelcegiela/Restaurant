package pi.restaurantapp.objects.enums

import android.content.Context
import pi.restaurantapp.R

enum class OrderType(val stringResourceId: Int) {
    IN_RESTAURANT(R.string.in_restaurant),
    PHONE_CALL(R.string.phone_call),
    CLIENT_APP(R.string.client_app);

    companion object {
        @JvmStatic
        fun getString(id: Int, context: Context): String {
            return context.getString(values()[id].stringResourceId)
        }

        fun getArrayOfStrings(context: Context): Array<String> {
            return values().map { context.getString(it.stringResourceId) }.toTypedArray()
        }
    }
}