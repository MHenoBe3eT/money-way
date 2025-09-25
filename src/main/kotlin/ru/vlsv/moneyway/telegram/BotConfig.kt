package ru.vlsv.moneyway.telegram

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

/**
 * Конфигурация для телеграм бота.
 * Необходима для его регистрации.
 */
@Configuration
class BotConfig(
    private val telegramBot: TelegramBot
) {
    @Bean
    fun telegramBotsApi(): TelegramBotsApi {
        val api = TelegramBotsApi(DefaultBotSession::class.java)
        api.registerBot(telegramBot)
        return api
    }
}