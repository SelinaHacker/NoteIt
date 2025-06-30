package ac.at.fhstp.note_it.feature_note.domain.util

// Sealed class representing the type of ordering (ascending or descending)
sealed class OrderType {
    // Ascending order type
    object Ascending : OrderType()
    // Descending order type
    object Descending : OrderType()
}
