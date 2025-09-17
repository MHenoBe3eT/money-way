package ru.vlsv.moneyway.event

import org.slf4j.LoggerFactory
import ru.vlsv.moneyway.dto.Event
import ru.vlsv.moneyway.enums.ParserType
import ru.vlsv.moneyway.event.filter.EventFilter
import ru.vlsv.moneyway.parser.ParserManager
import ru.vlsv.moneyway.telegram.TelegramSender
import java.time.LocalDateTime

/**
 * Абстрактный класс джобы, которая будет парсить данные по определенным параметрам.
 */
abstract class EventJob(
    private val telegramSender: TelegramSender,
    private val cache: EventCache
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    abstract val parserType: ParserType
    abstract val filter: EventFilter
    abstract val url: String
    abstract val title: String

    fun run(parserManager: ParserManager) {
        /**
         * По типу парсера подбираем нужный для определнной задачи
         */
        val parser = parserManager.getParser(parserType)

        /**
         * Скармливаем этому парсеру нужную ссылку
         */
        val events = parser.parseEvents(url)

        /**
         * Фильтруем все полученные события по нужным конкретной реализации параметрам
         */
        val filtered = filter.filter(events)

        /**
         * Проверяем в кеше, отправляли ли мы это событие уже в телеграм.
         */
        val toSend = filtered.filter { cache.markIfNew(it) }

        /**
         * Если есть сообщения для отправки - шлем в телегу.
         * Если нет, то логгируем отсутствие подходящих событий принтом.
         */
        if (toSend.isNotEmpty()) {
            telegramSender.send(formatMessage(toSend))
        } else {
            log.info("Нет подходящих событий для '$title' — ${LocalDateTime.now()}")
        }
    }

    /**
     * Класс наследника обязан сам определить форматирование
     */
    abstract fun formatMessage(events: List<Event>): String
}