package net.leonyang.web.huoim.push.provider

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.lang.reflect.Type
import java.time.LocalDateTime
import javax.ws.rs.Consumes
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.ext.MessageBodyReader
import javax.ws.rs.ext.MessageBodyWriter
import javax.ws.rs.ext.Provider
import sun.plugin2.util.PojoUtil.toJson
import java.nio.charset.Charset
import java.io.OutputStreamWriter
import com.google.gson.stream.JsonWriter



/**
 * 用于设置Jersey的Json转换器
 * 用于替换JacksonJsonProvider
 * <p>
 * 该工具类完成了，把Http请求中的请求数据转换为Model实体，
 * 同时也实现了把返回的Model实体转换为Json字符串
 * 并输出到Http的返回体中。
 *
 * @param <T> 任意类型范型定义
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class GsonProvider<T>: MessageBodyReader<T>, MessageBodyWriter<T> {

    companion object {
        val gson: Gson
        val builder = GsonBuilder()
            // 序列化为null的字段
            .serializeNulls()
            // 仅仅处理带有@Expose注解的变量
            .excludeFieldsWithoutExposeAnnotation()
            // 支持Map
            .enableComplexMapKeySerialization()
        init {
            // 添加对Java8LocalDateTime时间类型的支持
            builder.registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeConverter())
            gson = builder.create()
        }

    }


    /**
     * 把Json的字符串数据, 转换为T类型的实例
     */
    override fun readFrom(
        type: Class<T>?,
        genericType: Type?,
        annotations: Array<out Annotation>?,
        mediaType: MediaType?,
        httpHeaders: MultivaluedMap<String, String>?,
        entityStream: InputStream?
    ): T {
        JsonReader(InputStreamReader(entityStream, "UTF-8")).use { reader ->
            return gson.fromJson<T>(
                reader,
                genericType
            )
        }
    }

    override fun isReadable(
        type: Class<*>?,
        genericType: Type?,
        annotations: Array<out Annotation>?,
        mediaType: MediaType?
    ): Boolean = true

    override fun isWriteable(
        type: Class<*>?,
        genericType: Type?,
        annotations: Array<out Annotation>?,
        mediaType: MediaType?
    ): Boolean = true

    /**
     * 把一个T类的实例输出到Http输出流中
     */
    override fun writeTo(
        t: T,
        type: Class<*>?,
        genericType: Type?,
        annotations: Array<out Annotation>?,
        mediaType: MediaType?,
        httpHeaders: MultivaluedMap<String, Any>?,
        entityStream: OutputStream?
    ) {
        //TypeAdapter<T> adapter = gson.getAdapter((TypeToken<T>) TypeToken.get(genericType));
        gson.newJsonWriter(OutputStreamWriter(entityStream, Charset.forName("UTF-8"))).use { jsonWriter ->
            gson.toJson(t, genericType, jsonWriter)
            jsonWriter.close()
        }
    }
}