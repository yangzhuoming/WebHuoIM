package net.leonyang.web.huoim.push.utils

import java.math.BigInteger
import com.oracle.util.Checksums.update
import java.security.MessageDigest
import java.util.Base64.getEncoder
import com.sun.deploy.util.Base64Wrapper.encodeToString
import java.util.*
import net.leonyang.web.huoim.push.provider.GsonProvider




object TextUtil {

    /**
     * 计算一个字符串的MD5信息
     *
     * @param str 字符串
     * @return MD5值
     */
    fun getMD5(str: String): String? {
        return try {
            // 生成一个MD5加密计算摘要
            val md = MessageDigest.getInstance("MD5")
            // 计算md5函数
            md.update(str.toByteArray())
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            BigInteger(1, md.digest()).toString(16)
        } catch (e: Exception) {
            e.printStackTrace()
            str
        }
    }

    /**
     * 对一个字符串进行Base64编码
     *
     * @param str 原始字符串
     * @return 进行Base64编码后的字符串
     */
    fun encodeBase64(str: String): String {
        return Base64.getEncoder().encodeToString(str.toByteArray())
    }

    /**
     * 把任意类的实例转换为Json字符串
     *
     * @param obj Object
     * @return Json字符串
     */
    fun toJson(obj: Any): String {
        return GsonProvider.gson.toJson(obj)
    }

}