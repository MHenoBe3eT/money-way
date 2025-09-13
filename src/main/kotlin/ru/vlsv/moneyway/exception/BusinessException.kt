package ru.vlsv.moneyway.exception

open class BusinessException(message: String) : RuntimeException(message)

class GetEventsException(message: String) : BusinessException(message)
