package ru.vlsv.moneyway.parser

import org.springframework.stereotype.Component
import ru.vlsv.moneyway.enums.ParserType

@Component
class ParserManager(
    private val parsers: List<EventsParser>
) {
    fun getParser(parserType: ParserType): EventsParser {
        return when (parserType) {
            ParserType.MONEYWAY_1X2 -> parsers.find { it is MoneyWay1x2Parser } as EventsParser
            ParserType.DROPPING_ODDS -> parsers.find { it is DroppingOddsParser } as EventsParser
        }
    }
}