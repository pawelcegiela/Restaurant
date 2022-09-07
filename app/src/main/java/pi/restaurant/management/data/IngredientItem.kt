package pi.restaurant.management.data

class IngredientItem : AbstractDataObject {
    //TODO Sprawy związane ze zmienianiem/dodawaniem/zmniejszaniem itemów
    var name: String = ""
    var amount: Double = 0.0
    var unit: Int = 0

    @Suppress("unused")
    constructor()

    constructor(id: String, name: String) {
        this.id = id
        this.name = name
    }
}