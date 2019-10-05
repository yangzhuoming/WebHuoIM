package net.leonyang.web.huoim.push.bean.api.account

import com.google.gson.annotations.Expose



class LoginModel {

    @Expose
    var account: String? = null
    @Expose
    var password: String? = null
    @Expose
    var pushId: String? = null

    companion object {
        // 校验
        fun check(model: LoginModel?): Boolean {
            return null != model
                    && !model.account.isNullOrEmpty()
                    && !model.password.isNullOrEmpty()
        }
    }

}