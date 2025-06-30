package ac.at.fhstp.note_it.feature_note.domain.util

// Sealed class to define different ways to order notes
sealed class NoteOrder(val orderType: OrderType) {
    // Order notes by title
    class Title(orderType: OrderType) : NoteOrder(orderType)
    // Order notes by date
    class Date(orderType: OrderType) : NoteOrder(orderType)
    // Order notes by color
    class Color(orderType: OrderType) : NoteOrder(orderType)

    // Copies the current order with a new order type (Ascending or Descending)
    fun copy(orderType: OrderType): NoteOrder {
        return when (this) {
            is Title -> Title(orderType)
            is Date -> Date(orderType)
            is Color -> Color(orderType)
        }
    }
}