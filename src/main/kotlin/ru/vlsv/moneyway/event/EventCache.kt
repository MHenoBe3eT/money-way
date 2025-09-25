package ru.vlsv.moneyway.event

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import org.springframework.stereotype.Component
import ru.vlsv.moneyway.dto.Event
import ru.vlsv.moneyway.heartbeat.HearBeat
import java.util.concurrent.TimeUnit

@Component
class EventCache : HearBeat {
    val cache: Cache<Event, Boolean> by lazy {
        CacheBuilder.newBuilder()
            .expireAfterWrite(60, TimeUnit.MINUTES)
            .build()
    }

    fun markIfNew(event: Event): Boolean {
        val isNew = cache.getIfPresent(event) == null
        if (isNew) {
            cache.put(event, true)
        }
        return isNew
    }

    override fun beat(): String {
        return "Текущий размер кэша ${cache.size()}"
    }
}