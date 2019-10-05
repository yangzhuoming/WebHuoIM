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
            // 返回当前的账户
            val rspModel = AccountRspModel(user)
            ResponseModel.buildOk(rspModel)
        } else {
            // 登录失败
            ResponseModel.buildLoginError()
        }
    }

}