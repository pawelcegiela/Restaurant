package pi.restaurantapp.logic.fragments.management.allergens

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.fragments.AbstractPreviewItemLogic

/**
 * Class responsible for business logic and communication with database (Model layer) for PreviewAllergenFragment.
 * @see pi.restaurantapp.ui.fragments.management.allergens.PreviewAllergenFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.allergens.PreviewAllergenViewModel ViewModel layer
 */
class PreviewAllergenLogic : AbstractPreviewItemLogic() {
    override val databasePath = "allergens"

    fun getContainingDishes(containingDishesIds: List<String>, callback: (ArrayList<String>) -> Unit) {
        if (containingDishesIds.isEmpty()) {
            callback(ArrayList())
            return
        }

        val containingDishes = ArrayList<String>()
        for (id in containingDishesIds) {
            Firebase.firestore.collection("dishes-basic").document(id).get().addOnSuccessListener { snapshot ->
                containingDishes.add(snapshot.getString("name") ?: "")
                if (containingDishes.size == containingDishesIds.size) {
                    callback(containingDishes)
                }
            }
        }
    }
}