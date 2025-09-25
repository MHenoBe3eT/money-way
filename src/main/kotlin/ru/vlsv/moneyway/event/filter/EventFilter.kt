package ru.vlsv.moneyway.event.filter

import ru.vlsv.moneyway.dto.Event

/**
 * Данный интерфейс реализуют классы, которые будут осуществлять фильтрацию
 * распаршенных events. Каждая реализация фильтрует по своему.
 */
interface EventFilter {
    fun filter(events: List<Event>): List<Event>
}