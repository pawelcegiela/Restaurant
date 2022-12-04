package pi.restaurantapp.logic.fragments.management.allergens

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.fragments.AbstractPreviewItemLogic

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