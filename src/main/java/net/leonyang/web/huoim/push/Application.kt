package net.leonyang.web.huoim.push

import net.leonyang.web.huoim.push.provider.GsonProvider
import net.leonyang.web.huoim.push.service.AccountService
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider
import org.glassfish.jersey.server.ResourceConfig
import java.util.logging.Logger

class Application: ResourceConfig() {

    init {
        //注册逻辑处理的包名
        packages(AccountService::class.java.name)

        //注册Json解析器
        //register(JacksonJsonProvider::class)
        // 替换解析器为Gson
        register(
            GsonProvider::class)

        //注册日志打印输出
        register(Logger::class)

    }


}