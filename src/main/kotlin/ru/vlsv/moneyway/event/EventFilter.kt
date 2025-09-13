package ru.vlsv.moneyway.event

import ru.vlsv.moneyway.dto.Event

interface EventFilter {
    fun filter(events: List<Event>): List<Event>
}

class DrawFilter(private val minAmount: Double) : EventFilter {
    override fun filter(events: List<Event>) = events.filter { e ->
        val draw = e.drawAmount
        draw != null && draw > minAmount &&
                (e.homeAmount ?: 0.0) < draw &&
                (e.awayAmount ?: 0.0) < draw
    }
}