package pi.restaurant.management.enums

enum class IngredientItemState {
    BASE,
    OTHER,
    POSSIBLE,
    CHANGE_AMOUNT,
    REMOVE;

    companion object {
        fun isActionRemove(state: IngredientItemState): Boolean {
            return state != CHANGE_AMOUNT
        }

        fun isActionAdd(state: IngredientItemState): Boolean {
            return state == BASE || state == OTHER || state == POSSIBLE
        }
    }
}