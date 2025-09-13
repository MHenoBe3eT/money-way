package ru.vlsv.moneyway.processor


import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.vlsv.moneyway.dto.Event
import ru.vlsv.moneyway.enums.ParserType
import ru.vlsv.moneyway.parser.ParserManager
import ru.vlsv.moneyway.telegram.TelegramSender
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Component
class EventsProcessor(
    private val parserManager: ParserManager,
    private val telegramSender: TelegramSender
) {
    private val sentEventsCache: Cache<Event, Boolean> = CacheBuilder.newBuilder()
        .expireAfterWrite(60, TimeUnit.MINUTES)
        .build()

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    fun processLiveEvents() {
        val parser = parserManager.getParser(ParserType.MONEYWAY_1X2)
        val events = parser.parseEvents("https://arbworld.net/ru/denezhnyye-potoki/football-1-x-2-live")

        val eventsToSend = filterDrawEvents(events)

        val message = eventsToSend.joinToString("\n\n") { event ->
            """
            Ставка лайв
            ${event.league}
            ${event.date} ${event.home} - ${event.away}
            П1 ${event.homeAmount ?: "-"} X ${event.drawAmount ?: "-"} П2 ${event.awayAmount ?: "-"}
            """.trimIndent()
        }

        if (eventsToSend.isNotEmpty()) {
            telegramSender.send(message)
        } else {
            println("Нет подходящих ставок лайв " + LocalDateTime.now().toString())
        }
    }

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    fun processUpcomingEvents() {
        val parser = parserManager.getParser(ParserType.MONEYWAY_1X2)
        val events =
            parser.parseEvents("https://arbworld.net/ru/denezhnyye-potoki/football-1-x-2.php?hidden=&shown=&timeZone=%2B03%3A00&day=Today&refreshInterval=60&order=24&min=0&max=100")

        val eventsToSend = filterDrawEvents(events)

        val message = eventsToSend.joinToString("\n\n") { event ->
            """
            Предстоящее событие
            ${event.league}
            ${event.date} ${event.home} - ${event.away}
            П1 ${event.homeAmount ?: "-"} X ${event.drawAmount ?: "-"} П2 ${event.awayAmount ?: "-"}
            """.trimIndent()
        }

        if (eventsToSend.isNotEmpty()) {
            telegramSender.send(message)
        } else {
            println("Подходящих ставок на сегодня пока нет " + LocalDateTime.now().toString())
        }
    }

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    fun processDrawDroppingOdds() {
        val parser = parserManager.getParser(ParserType.DROPPING_ODDS)
        val events =
            parser.parseEvents("https://arbworld.net/ru/padayushchiye-koeffitsiyenty/football-1-x-2?hidden=&shown=&timeZone=%2B03%3A00&refreshInterval=60&order=Drop&min=0&max=100&day=Today")

        val eventsToSend = events.filter { it.drawAmount != null && it.drawAmount < 2.6 }

        val message = eventsToSend.joinToString("\n\n") { event ->
            """
            Подозрительно низкий коэффициент на ничью
            ${event.league}
            ${event.date} ${event.home} - ${event.away}
            X ${event.drawAmount ?: "-"}
            """.trimIndent()
        }

        if (eventsToSend.isNotEmpty()) {
            telegramSender.send(message)
        } else {
            println("На сегодня нет подозрительных коэффициентов на ничью " + LocalDateTime.now().toString())
        }
    }

    fun filterDrawEvents(events: List<Event>): List<Event> {
        return events.filter { event ->
            val draw = event.drawAmount
            val passesFilter = draw != null &&
                    draw > 10_000 &&
                    (event.homeAmount ?: 0.0) < draw &&
                    (event.awayAmount ?: 0.0) < draw

            val notSent = sentEventsCache.getIfPresent(event) == null
            if (passesFilter && notSent) {
                sentEventsCache.put(event, true)
            }

            passesFilter && notSent
        }
    }

}