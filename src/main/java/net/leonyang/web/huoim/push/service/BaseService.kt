package net.leonyang.web.huoim.push.service

import javax.ws.rs.core.Context
import javax.ws.rs.core.SecurityContext
import net.leonyang.web.huoim.push.bean.db.User

open class BaseService {

    // 添加一个上下文注解，该注解会给securityContext赋值
    // 具体的值为我们的拦截器中所返回的SecurityContext
    @Context
    protected var securityContext: SecurityContext? = null

    /**
     * 从上下文中直接获取自己的信息
     *
     * @return User
     */
    protected fun getSelf(): User {
        return securityContext?.userPrincipal as User
    }

}