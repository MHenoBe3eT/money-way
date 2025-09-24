package ru.vlsv.moneyway.event.filter

import ru.vlsv.moneyway.dto.Event

/**
 * Оставляет те события, в которых прогруз на ничью больше заданного
 * И если прогруз на ничью при этом больше, чем на П1 или на П2
 */
class MoneyWayToDrawEventFilterImpl(
    private val minAmount: Double
) : EventFilter {

    override fun filter(events: List<Event>): List<Event> {
        return events.filter { event ->

            event.drawAmount != null && event.drawAmount > minAmount &&
                    (event.homeAmount ?: 0.0) < event.drawAmount &&
                    (event.awayAmount ?: 0.0) < event.drawAmount
        }
    }
}