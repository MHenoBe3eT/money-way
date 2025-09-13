package ru.vlsv.moneyway.event

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.vlsv.moneyway.event.jobs.LiveMoneyWay1x2
import ru.vlsv.moneyway.parser.ParserManager
import java.util.concurrent.TimeUnit

@Component
class JobsScheduler(
    private val liveMoneyWay1x2: LiveMoneyWay1x2,
    private val parserManager: ParserManager
) {

//    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    fun processLive() = liveMoneyWay1x2.run(parserManager)

//    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
//    fun processUpcoming() = upcomingJob.run(parserManager)
}