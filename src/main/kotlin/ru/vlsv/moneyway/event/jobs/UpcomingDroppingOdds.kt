package ru.vlsv.moneyway.event.jobs

import org.springframework.stereotype.Component
import ru.vlsv.moneyway.dto.Event
import ru.vlsv.moneyway.enums.ParserType
import ru.vlsv.moneyway.event.EventCache
import ru.vlsv.moneyway.event.EventJob
import ru.vlsv.moneyway.event.filter.DroppingOddsToDrawEventFilterImpl
import ru.vlsv.moneyway.event.filter.EventFilter
import ru.vlsv.moneyway.telegram.TelegramSender

@Component
class UpcomingDroppingOdds(
    telegramSender: TelegramSender,
    cache: EventCache,
) : EventJob(telegramSender, cache) {
    override val parserType: ParserType = ParserType.DROPPING_ODDS
    override val filter: EventFilter = DroppingOddsToDrawEventFilterImpl(minOdd = 2.6)
    override val url: String =
        "https://arbworld.net/ru/padayushchiye-koeffitsiyenty/football-1-x-2?hidden=&shown=&timeZone=%2B03%3A00&refreshInterval=60&order=Drop&min=0&max=100&day=Today"
    override val title: String = "Dropping Odds to draw"

    override fun formatMessage(events: List<Event>): String {
        return events.joinToString("\n\n") { event ->
            """
            Подозрительно низкий коэффициент на ничью
            ${event.league}
            ${event.date} ${event.home} - ${event.away}
            X ${event.drawAmount ?: "-"}
            """.trimIndent()
        }
    }
}