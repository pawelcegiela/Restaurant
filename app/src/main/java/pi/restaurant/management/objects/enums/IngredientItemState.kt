package pi.restaurant.management.objects.enums

enum class IngredientItemState {
    BASE,
    OTHER,
    POSSIBLE,
    CHANGE_AMOUNT,
    CHANGE_EXTRA_PRICE,
    REMOVE;

    companion object {
        fun isActionRemove(state: IngredientItemState): Boolean {
            return state == BASE || state == OTHER || state == POSSIBLE || state == REMOVE
        }

        fun isActionAdd(state: IngredientItemState): Boolean {
            return state == BASE || state == OTHER || state == POSSIBLE
        }
    }
}