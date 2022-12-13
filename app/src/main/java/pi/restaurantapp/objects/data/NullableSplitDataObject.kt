package pi.restaurantapp.objects.data

/**
 * Data transfer class containing item id, nullable basic information and nullable details of any AbstractDataObject.
 */
class NullableSplitDataObject(var id: String, var basic: AbstractDataObject?, var details: AbstractDataObject?)