package ru.vlsv.moneyway.heartbeat

/**
 * Данный интерфейс реализован для проверки текущего состояния сервиса.
 * По запросу снаружи будут возвращаться данные.
 */
interface HearBeat {
    fun beat(): String
}