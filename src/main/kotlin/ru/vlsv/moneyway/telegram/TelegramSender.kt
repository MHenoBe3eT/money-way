package ru.vlsv.moneyway.telegram

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class TelegramSender(
    @Value("\${telegram.token}") private val token: String,
    @Value("\${telegram.chat-id}") private val chatId: String
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val restTemplate = RestTemplate()
    private val apiUrl = "https://api.telegram.org/bot$token/sendMessage"

    fun send(message: String) {
        try {
            val payload = mapOf(
                "chat_id" to chatId,
                "text" to message
            )
            restTemplate.postForObject(apiUrl, payload, String::class.java)
        } catch (e: Exception) {
            log.error("Не удалось отправить сообщение в Telegram: {}", e.message)
        }
    }
}