package ru.vlsv.moneyway.event.filter

import ru.vlsv.moneyway.dto.Event

/**
 * Фильтрует события, в которых коэффициент на ничью ниже заданного
 */
class DroppingOddsToDrawEventFilterImpl(
    private val minOdd: Double
) : EventFilter {
    override fun filter(events: List<Event>): List<Event> {
        return events.filter { event ->
            event.drawAmount != null && event.drawAmount < minOdd
        }
    }
}