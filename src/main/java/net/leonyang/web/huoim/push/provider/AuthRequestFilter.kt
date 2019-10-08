package net.leonyang.web.huoim.push.provider

import net.leonyang.web.huoim.push.bean.api.base.ResponseModel
import net.leonyang.web.huoim.push.factory.UserFactory
import org.glassfish.jersey.server.ContainerRequest
import java.io.InputStream
import java.security.Principal
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerRequestFilter
import javax.ws.rs.core.Response
import javax.ws.rs.core.SecurityContext
import javax.ws.rs.ext.Provider

/**
 * 用于所有的请求的接口的过滤和拦截
 */
@Provider
class AuthRequestFilter: ContainerRequestFilter {

    /**
     * 实现接口的过滤方法
     */
    override fun filter(requestContext: ContainerRequestContext?) {
        // 检查是否是登录注册接口
        val relationPath = (requestContext as ContainerRequest).getPath(false)
        if (relationPath.startsWith("account/login")
            || relationPath.startsWith("account/register")) {
            // 直接走正常逻辑，不做拦截
            return
        }

        //从Headers中去找到第一个token节点
        val token = requestContext.headers.getFirst("token")
        if(!token.isNullOrEmpty()){
            // 查询自己的信息
            val self = UserFactory.findByToken(token)
            if(null != self) {
                // 给当前请求添加一个上下文
                requestContext.securityContext = object: SecurityContext {
                    override fun isUserInRole(role: String?): Boolean {
                        // 可以在这里写入用户的权限，role 是权限名，
                        // 可以管理管理员权限等等
                        return true
                    }

                    override fun getAuthenticationScheme(): String? {
                        // 不用理会
                        return null
                    }

                    // 主体部分
                    override fun getUserPrincipal(): Principal {
                        return self // User 实现 Principal接口
                    }

                    override fun isSecure(): Boolean {
                        // 默认false即可，HTTPS
                        return false
                    }
                }
                // 写入上下文后就返回
                return
            }
        }

        // 直接返回一个账户需要登录的Model
        val model = ResponseModel.buildAccountError<Any>()
        // 构建一个返回
        val response = Response.status(Response.Status.OK)
            .entity(model)
            .build()

        // 拦截，停止一个请求的继续下发，调用该方法后之间返回请求
        // 不会走到Service中去
        requestContext.abortWith(response)
    }
}