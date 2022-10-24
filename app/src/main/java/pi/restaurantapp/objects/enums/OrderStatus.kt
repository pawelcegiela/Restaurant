package pi.restaurantapp.objects.enums

import android.content.Context
import pi.restaurantapp.R

enum class OrderStatus(val stringResourceId: Int) {
    NEW(R.string.new_s),
    ACCEPTED(R.string.accepted),
    PREPARING(R.string.preparing),
    READY(R.string.ready),
    DELIVERY(R.string.delivery),
    FINISHED(R.string.finished),
    CLOSED_WITHOUT_REALIZATION(R.string.closed_without_realization);

    companion object {
        fun getString(id: Int, context: Context): String {
            return context.getString(values()[id].stringResourceId)
        }

        fun getArrayOfStrings(context: Context): Array<String> {
            return values().map { context.getString(it.stringResourceId) }.toTypedArray()
        }

        fun getArrayOfStringsForDelivery(context: Context): Array<String> {
            val statuses = getStatusesForDelivery()
            return values().filter{ it.ordinal in statuses }.map { context.getString(it.stringResourceId) }.toTypedArray()
        }

        fun getArrayOfStringsForSelfPickup(context: Context): Array<String> {
            val statuses = getStatusesForSelfPickup()
            return values().filter{ it.ordinal in statuses }.map { context.getString(it.stringResourceId) }.toTypedArray()
        }

        fun getNextStatusId(currentStatusId: Int, collectionType: CollectionType): Int {
            val statuses = if (collectionType == CollectionType.DELIVERY) {
                getStatusesForDelivery()
            } else {
                getStatusesForSelfPickup()
            }

            val index = statuses.indexOf(currentStatusId)
            return if (index < statuses.size - 1 && index >= 0) {
                statuses[index + 1]
            } else {
                currentStatusId
            }
        }

        fun getStatusesForDelivery(): ArrayList<Int> {
            return arrayListOf(NEW.ordinal, ACCEPTED.ordinal, PREPARING.ordinal, DELIVERY.ordinal, FINISHED.ordinal)
        }

        fun getStatusesForSelfPickup(): ArrayList<Int> {
            return arrayListOf(NEW.ordinal, ACCEPTED.ordinal, PREPARING.ordinal, READY.ordinal, FINISHED.ordinal)
        }

        fun isFinished(ordinal: Int) : Boolean {
            return ordinal == FINISHED.ordinal || ordinal == CLOSED_WITHOUT_REALIZATION.ordinal
        }
    }
}