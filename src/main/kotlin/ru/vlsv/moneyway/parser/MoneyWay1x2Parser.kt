package ru.vlsv.moneyway.parser

import org.springframework.stereotype.Component
import ru.vlsv.moneyway.dto.Event
import ru.vlsv.moneyway.service.ParserService

@Component
class MoneyWay1x2Parser(
    private val parserService: ParserService
) : EventsParser {

    override fun parseEvents(url: String): List<Event> {
        val doc = parserService.getDocument(url)

        val rows = doc.select("tbody tr.belowHeader")
        val result = mutableListOf<Event>()

        for (row in rows) {
            val league = row.selectFirst("td.tleague")?.text()?.trim() ?: continue
            val date = row.selectFirst("td.tdate")?.text()?.trim()?.let { parserService.parseDate(it) } ?: continue
            val home = row.selectFirst("td.thome")?.text()?.trim() ?: continue
            val away = row.selectFirst("td.taway")?.text()?.trim() ?: continue

            val oddsCells = row.select("td.odds_col_small")
            val homeOdds = oddsCells.getOrNull(0)?.text()?.toDoubleOrNull()
            val drawOdds = oddsCells.getOrNull(1)?.text()?.toDoubleOrNull()
            val awayOdds = oddsCells.getOrNull(2)?.text()?.toDoubleOrNull()

            val amountCells = row.select("td.odds_col")

            val homeAmount = parserService.parsePounds(amountCells.getOrNull(0)?.text())
            val drawAmount = parserService.parsePounds(amountCells.getOrNull(1)?.text())
            val awayAmount = parserService.parsePounds(amountCells.getOrNull(2)?.text())

            result.add(
                Event(
                    league, date, home, away,
                    homeOdds, drawOdds, awayOdds,
                    homeAmount, drawAmount, awayAmount
                )
            )
        }

        return result
    }
}
