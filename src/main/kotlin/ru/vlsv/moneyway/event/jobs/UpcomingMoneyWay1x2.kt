package ru.vlsv.moneyway.event.jobs

import org.springframework.stereotype.Component
import ru.vlsv.moneyway.dto.Event
import ru.vlsv.moneyway.enums.ParserType
import ru.vlsv.moneyway.event.EventCache
import ru.vlsv.moneyway.event.EventJob
import ru.vlsv.moneyway.event.filter.EventFilter
import ru.vlsv.moneyway.event.filter.MoneyWayToDrawEventFilterImpl
import ru.vlsv.moneyway.telegram.TelegramBot

@Component
class UpcomingMoneyWay1x2(
    telegramBot: TelegramBot,
    cache: EventCache,
) : EventJob(telegramBot, cache) {
    override val parserType: ParserType = ParserType.MONEYWAY_1X2
    override val filter: EventFilter = MoneyWayToDrawEventFilterImpl(minAmount = 10_000.0)
    override val url: String =
        "https://arbworld.net/ru/denezhnyye-potoki/football-1-x-2.php?hidden=&shown=&timeZone=%2B03%3A00&day=Today&refreshInterval=60&order=24&min=0&max=100"
    override val title: String = "Предстоящие. Money way ничья"

    override fun formatMessage(events: List<Event>): String {
        return events.joinToString("\n\n") { event ->
            """
            Предстоящее событие
            ${event.league}
            ${event.date} ${event.home} - ${event.away}
            П1 ${event.homeAmount ?: "-"} X ${event.drawAmount ?: "-"} П2 ${event.awayAmount ?: "-"}
            """.trimIndent()
        }
    }
}