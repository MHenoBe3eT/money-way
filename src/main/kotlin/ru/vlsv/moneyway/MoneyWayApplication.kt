package ru.vlsv.moneyway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class MoneyWayApplication

fun main(args: Array<String>) {
    runApplication<MoneyWayApplication>(*args)
}
