package ru.vlsv.moneyway.dto

data class Event(
    val league: String,
    val date: String,
    val home: String,
    val away: String,
    val homeOdds: Double?,
    val drawOdds: Double?,
    val awayOdds: Double?,
    val homeAmount: Double?,
    val drawAmount: Double?,
    val awayAmount: Double?
)