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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Event) return false

        return league == other.league &&
                date == other.date &&
                home == other.home &&
                away == other.away
    }

    override fun hashCode(): Int {
        var result = league.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + home.hashCode()
        result = 31 * result + away.hashCode()
        return result
    }
}
