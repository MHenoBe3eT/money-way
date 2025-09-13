package ru.vlsv.moneyway.event

import ru.vlsv.moneyway.dto.Event
import ru.vlsv.moneyway.enums.ParserType
import ru.vlsv.moneyway.parser.ParserManager
import ru.vlsv.moneyway.telegram.TelegramSender
import java.time.LocalDateTime

abstract class EventJob(
    private val telegramSender: TelegramSender,
    private val cache: EventCache
) {
    abstract val parserType: ParserType
    abstract val filter: EventFilter
    abstract val url: String
    abstract val title: String

    fun run(parserManager: ParserManager) {
        val parser = parserManager.getParser(parserType)
        val events = parser.parseEvents(url)

        val filtered = filter.filter(events)
        val toSend = filtered.filter { cache.markIfNew(it) }

        if (toSend.isNotEmpty()) {
            telegramSender.send(formatMessage(toSend))
        } else {
            println("Нет подходящих событий для '$title' — ${LocalDateTime.now()}")
        }
    }

    private fun formatMessage(events: List<Event>): String =
        events.joinToString("\n\n") { e ->
            "$title\n${e.league}\n${e.date} ${e.home} - ${e.away}\nП1 ${e.homeAmount ?: "-"} X ${e.drawAmount ?: "-"} П2 ${e.awayAmount ?: "-"}"
        }
}