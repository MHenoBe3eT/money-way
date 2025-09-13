package ru.vlsv.moneyway.parser

import ru.vlsv.moneyway.dto.Event

interface EventsParser {
    fun parseEvents(url: String): List<Event>
}