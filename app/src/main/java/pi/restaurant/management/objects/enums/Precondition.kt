package pi.restaurant.management.objects.enums

import pi.restaurant.management.R

enum class Precondition(val nameRes: Int) {
    OK(0),
    PASSWORDS_DIFFER(R.string.passwords_differ),
    PASSWORD_TOO_WEAK(R.string.password_too_weak),
    TOO_LOW_ROLE(R.string.you_can_set_lower_roles),
    OPENING_HOURS_FORMAT(R.string.enter_correct_hours),
    SAME_USER(0)
}