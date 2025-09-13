package ru.vlsv.moneyway.service

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Component
import ru.vlsv.moneyway.exception.GetEventsException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.*

@Component
class ParserService {

    private val arbWorldFormatter: DateTimeFormatter by lazy {
        DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("dd.MMM HH:mm:ss")
            .parseDefaulting(ChronoField.YEAR, LocalDateTime.now().year.toLong())
            .toFormatter(Locale.ENGLISH)
    }

    fun getDocument(url: String): Document {
        repeat(3) { attempt ->
            runCatching {
                return Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                    .timeout(20_000)
                    .get()
            }.onFailure {
                if (attempt < 2) Thread.sleep(2000)
            }
        }
        throw GetEventsException("Не удалось получить документ с $url после 3 попыток")
    }

    fun parsePounds(text: String?): Double? =
        text?.substringAfter("£", "")
            ?.replace(Regex("[^0-9,]"), "")
            ?.replace("\\s".toRegex(), "")
            ?.replace(",", ".")
            ?.takeIf { it.isNotEmpty() }
            ?.toDoubleOrNull()

    fun parseDroppingOdds(text: String?): Double? = text?.substringAfter(" ")?.toDoubleOrNull()

    fun parseDate(raw: String): String =
        runCatching {
            LocalDateTime.parse(raw, arbWorldFormatter)
                .plusHours(3)
                .format(arbWorldFormatter)
        }.getOrDefault(raw)
}