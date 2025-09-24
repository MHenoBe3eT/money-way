package ru.vlsv.moneyway.event

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.vlsv.moneyway.parser.ParserManager
import java.util.concurrent.TimeUnit

/**
 * Планировщик задач.
 * Можно заинжектить и запустить каждую задачу отдельно,
 * а можно запускать все сразу одной функцией.
 */
@Component
class JobsScheduler(
    private val eventJobs: List<EventJob>,
    private val parserManager: ParserManager
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * Запускаем все доступные джобы сразу
     */
    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    fun runAll() = eventJobs.forEach {
        try {
            it.run(parserManager)
        } catch (e: Exception) {
            log.error("задача ${it.title} завершилась с ошибкой", e)
        }
    }
}