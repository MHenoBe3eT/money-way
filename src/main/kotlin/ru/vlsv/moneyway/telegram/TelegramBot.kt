package ru.vlsv.moneyway.telegram

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.vlsv.moneyway.heartbeat.HearBeat

/**
 * За рассылку сообщений отвечает LongPollingBot.
 * Для ответа пока будет доступна 1 команда - 'heartbeat'
 * В ответ на эту команду будет отправлена информация о текущем состоянии.
 */
@Component
class TelegramBot(
    @Value("\${telegram.token}") private val botToken: String,
    @Value("\${telegram.bot.username}") private val botUsername: String,
    @Value("\${telegram.chat-id}") private val targetGroupChatId: String,
    private val heartBeatPatients: List<HearBeat>
) : TelegramLongPollingBot(botToken) {
    override fun getBotUsername(): String {
        return botUsername
    }

    override fun onUpdateReceived(update: Update?) {
        update?.let {
            if (update.message.text.equals("heartbeat", ignoreCase = true)) {
                val chatId = update.message.chatId.toString()

                heartBeatPatients.forEach { patient ->
                    val message = SendMessage(chatId, patient.beat())
                    execute(message)
                }
            }
        }
    }

    fun sendEventsNotification(text: String) {
        val message = SendMessage(targetGroupChatId, text)
        execute(message)
    }
}