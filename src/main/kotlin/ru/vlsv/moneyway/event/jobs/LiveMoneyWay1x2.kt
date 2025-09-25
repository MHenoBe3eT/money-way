package ru.vlsv.moneyway.event.jobs

import org.springframework.stereotype.Component
import ru.vlsv.moneyway.dto.Event
import ru.vlsv.moneyway.enums.ParserType
import ru.vlsv.moneyway.event.EventCache
import ru.vlsv.moneyway.event.EventJob
import ru.vlsv.moneyway.event.filter.EventFilter
import ru.vlsv.moneyway.event.filter.MoneyWayToDrawEventFilterImpl
import ru.vlsv.moneyway.telegram.TelegramBot

/**
 * Парсит лайв события на предмет прогруза на ничью
 */
@Component
class LiveMoneyWay1x2(
    telegramBot: TelegramBot,
    cache: EventCache,
) : EventJob(telegramBot, cache) {

    override val parserType = ParserType.MONEYWAY_1X2
    override val filter: EventFilter = MoneyWayToDrawEventFilterImpl(minAmount = 10_000.0)
    override val url = "https://arbworld.net/ru/denezhnyye-potoki/football-1-x-2-live"
    override val title = "Ставка лайв"

    override fun formatMessage(events: List<Event>): String {
        return events.joinToString("\n\n") { event ->
            """
            Ставка лайв
            ${event.league}
            ${event.date} ${event.home} - ${event.away}
            П1 ${event.homeAmount ?: "-"} X ${event.drawAmount ?: "-"} П2 ${event.awayAmount ?: "-"}
            """.trimIndent()
        }
    }
}