package net.leonyang.web.huoim.push.bean.api.base

import java.io.Serializable
import com.google.gson.annotations.Expose
import java.time.LocalDateTime



class ResponseModel<M>: Serializable {

    companion object {
        // 成功
        val SUCCEED = 1
        // 未知错误
        val ERROR_UNKNOWN = 0

        // 没有找到用户信息
        val ERROR_NOT_FOUND_USER = 4041
        // 没有找到群信息
        val ERROR_NOT_FOUND_GROUP = 4042
        // 没有找到群成员信息
        val ERROR_NOT_FOUND_GROUP_MEMBER = 4043

        // 创建用户失败
        val ERROR_CREATE_USER = 3001
        // 创建群失败
        val ERROR_CREATE_GROUP = 3002
        // 创建群成员失败
        val ERROR_CREATE_MESSAGE = 3003

        // 请求参数错误
        val ERROR_PARAMETERS = 4001
        // 请求参数错误-已存在账户
        val ERROR_PARAMETERS_EXIST_ACCOUNT = 4002
        // 请求参数错误-已存在名称
        val ERROR_PARAMETERS_EXIST_NAME = 4003

        // 服务器错误
        val ERROR_SERVICE = 5001

        // 账户Token错误，需要重新登录
        val ERROR_ACCOUNT_TOKEN = 2001
        // 账户登录失败
        val ERROR_ACCOUNT_LOGIN = 2002
        // 账户注册失败
        val ERROR_ACCOUNT_REGISTER = 2003
        // 没有权限操作
        val ERROR_ACCOUNT_NO_PERMISSION = 2010

        fun <M> buildOk(): ResponseModel<M> {
            return ResponseModel()
        }

        fun <M> buildOk(result: M): ResponseModel<M> {
            return ResponseModel(result)
        }

        fun <M> buildParameterError(): ResponseModel<M> {
            return ResponseModel(ERROR_PARAMETERS, "Parameters Error.")
        }

        fun <M> buildHaveAccountError(): ResponseModel<M> {
            return ResponseModel(ERROR_PARAMETERS_EXIST_ACCOUNT, "Already have this account.")
        }

        fun <M> buildHaveNameError(): ResponseModel<M> {
            return ResponseModel(ERROR_PARAMETERS_EXIST_NAME, "Already have this name.")
        }

        fun <M> buildServiceError(): ResponseModel<M> {
            return ResponseModel(ERROR_SERVICE, "Service Error.")
        }

        fun <M> buildNotFoundUserError(str: String?): ResponseModel<M> {
            return ResponseModel(ERROR_NOT_FOUND_USER, str ?: "Not Found User.")
        }

        fun <M> buildNotFoundGroupError(str: String?): ResponseModel<M> {
            return ResponseModel(ERROR_NOT_FOUND_GROUP, str ?: "Not Found Group.")
        }

        fun <M> buildNotFoundGroupMemberError(str: String?): ResponseModel<M> {
            return ResponseModel(ERROR_NOT_FOUND_GROUP_MEMBER, str ?: "Not Found GroupMember.")
        }

        fun <M> buildAccountError(): ResponseModel<M> {
            return ResponseModel(ERROR_ACCOUNT_TOKEN, "Account Error; you need login.")
        }

        fun <M> buildLoginError(): ResponseModel<M> {
            return ResponseModel(ERROR_ACCOUNT_LOGIN, "Account or password error.")
        }

        fun <M> buildRegisterError(): ResponseModel<M> {
            return ResponseModel(ERROR_ACCOUNT_REGISTER, "Have this account.")
        }

        fun <M> buildNoPermissionError(): ResponseModel<M> {
            return ResponseModel(ERROR_ACCOUNT_NO_PERMISSION, "You do not have permission to operate.")
        }

        fun <M> buildCreateError(type: Int): ResponseModel<M> {
            return ResponseModel(type, "Create failed.")
        }

    }

    @Expose
    var code: Int = 0
    @Expose
    var message: String? = null
    @Expose
    var time = LocalDateTime.now()
    @Expose
    var result: M? = null

    constructor() {
        code = 1
        message = "ok"
    }

    constructor(result: M):this() {
        this.result = result
    }

    constructor(code: Int, message: String):this (){
        this.code = code
        this.message = message
    }

    constructor(code: Int, message: String, result: M): this(code, message) {
        this.result = result
    }

}