package net.leonyang.web.huoim.push.service

import net.leonyang.web.huoim.push.bean.api.account.AccountRspModel
import net.leonyang.web.huoim.push.bean.api.account.LoginModel
import net.leonyang.web.huoim.push.bean.api.account.RegisterModel
import net.leonyang.web.huoim.push.bean.api.base.ResponseModel
import net.leonyang.web.huoim.push.bean.card.UserCard
import net.leonyang.web.huoim.push.bean.db.User
import net.leonyang.web.huoim.push.factory.UserFactory
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

// http://localhost:8080/api/account
@Path("/account")
class AccountService {

    //http://localhost:8080/api/account/register
    @POST
    @Path("/register")
    // 指定请求与返回的相应体为JSON
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun register(model: RegisterModel): ResponseModel<AccountRspModel>? {
        if (!RegisterModel.check(model)) {
            // 返回参数异常
            return ResponseModel.buildParameterError()
        }

        var user = UserFactory.findByPhone(model.account?.trim())
        if (null != user) {
            // 已有账户
            return ResponseModel.buildHaveAccountError()
        }

        user = UserFactory.findByName(model.name?.trim())
        if (null != user) {
            // 已有用户名
            return ResponseModel.buildHaveNameError()
        }

        user = UserFactory.register(model.account!!, model.password!!, model.name!!)
        if (null != user) {
            // 返回当前的账户
            val rspModel = AccountRspModel(user)
            return ResponseModel.buildOk(rspModel)
        } else {
            // 注册异常
            return ResponseModel.buildRegisterError()
        }
    }

    // 登录
    @POST
    @Path("/login")
    // 指定请求与返回的相应体为JSON
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun login(model: LoginModel): ResponseModel<AccountRspModel> {
        if (!LoginModel.check(model)) {
            // 返回参数异常
            return ResponseModel.buildParameterError()
        }

        val user = UserFactory.login(model.account!!, model.password!!)
        return if (user != null) {
            // 如果有携带PushId
            if (!model.pushId.isNullOrEmpty()) {
                return bind(user, model.pushId!!)
            }

            // 返回当前的账户
            val rspModel = AccountRspModel(user)
            ResponseModel.buildOk(rspModel)
        } else {
            // 登录失败
            ResponseModel.buildLoginError()
        }
    }

    // 绑定设备Id
    @POST
    @Path("/bind/{pushId}")
    // 指定请求与返回的相应体为JSON
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    // 从请求头中获取token字段
    // pushId从url地址中获取
    fun bind(@HeaderParam("token") token: String,
             @PathParam("pushId") pushId: String): ResponseModel<AccountRspModel> {
        if (token.isNullOrEmpty() || pushId.isNullOrEmpty()) {
            // 返回参数异常
            return ResponseModel.buildParameterError()
        }
        // 拿到自己的个人信息
        val user = UserFactory.findByToken(token)
        if(null != user) {
            return bind(user, pushId)
        } else {
            // Token 失效，所有无法进行绑定
            return ResponseModel.buildAccountError()
        }
    }

    /**
     * 绑定的操作
     *
     * @param self   自己
     * @param pushId PushId
     * @return User
     */
    private fun bind(self: User, pushId: String): ResponseModel<AccountRspModel>{
        // 进行设备Id绑定的操作
        val user = UserFactory.bindPushId(self, pushId)
            ?: return ResponseModel.buildServiceError() // 绑定失败则是服务器异常

        // 返回当前的账户, 并且已经绑定了
        val rspModel = AccountRspModel(user, true)
        return ResponseModel.buildOk(rspModel)
    }

}