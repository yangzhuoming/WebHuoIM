package net.leonyang.web.huoim.push.provider

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.util.Locale
import java.time.format.DateTimeFormatter



/**
 * LocalDateTime 是一个Java8的新时间类型，
 * 使用起来比常规的Date更加Nice；
 * 但是Gson目前并没有默认支持对LocalDateTime的转换
 * <p>
 * 该工具类主要是为了解决LocalDateTime与Json字符串相互转换的问题
 *
 * @author qiujuer Email:qiujuer.live.cn
 */
class LocalDateTimeConverter: JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    /**
     * 时间转换的格式为：yyyy-MM-dd'T'HH:mm:ss.SSS
     */
    val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)

    /**
     * 把一个LocalDateTime格式的时间转换为Gson支持的JsonElement
     */
    override fun serialize(src: LocalDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(FORMATTER.format(src))
    }

    /**
     * 把一个Gson的JsonElement转换为LocalDateTime时间格式
     */
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDateTime {
        return FORMATTER.parse(json?.asString, LocalDateTime::from)
    }
}