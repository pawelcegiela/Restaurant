package pi.restaurant.management.objects.enums

import pi.restaurant.management.R

enum class Precondition(val nameRes: Int) {
    OK(0),
    PASSWORDS_DIFFER(R.string.passwords_differ),
    PASSWORD_TOO_WEAK(R.string.password_too_weak),
    TOO_LOW_ROLE(R.string.you_can_set_lower_roles),
    INVALID_OPENING_HOURS(R.string.opening_hour_cant_be_earlier_than_closing),
    SAME_USER(0),
    TOO_LOW_VALUE_DELIVERY(R.string.too_low_value_delivery)
}