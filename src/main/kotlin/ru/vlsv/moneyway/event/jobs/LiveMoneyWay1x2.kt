package ru.vlsv.moneyway.event.jobs

import org.springframework.stereotype.Component
import ru.vlsv.moneyway.enums.ParserType
import ru.vlsv.moneyway.event.DrawFilter
import ru.vlsv.moneyway.event.EventCache
import ru.vlsv.moneyway.event.EventFilter
import ru.vlsv.moneyway.event.EventJob
import ru.vlsv.moneyway.telegram.TelegramSender

@Component
class LiveMoneyWay1x2(
    telegramSender: TelegramSender,
    cache: EventCache
) : EventJob(telegramSender, cache) {

    override val parserType = ParserType.MONEYWAY_1X2
    override val filter: EventFilter = DrawFilter(minAmount = 10_000.0)
    override val url = "https://arbworld.net/ru/denezhnyye-potoki/football-1-x-2-live"
    override val title = "Ставка лайв"
}