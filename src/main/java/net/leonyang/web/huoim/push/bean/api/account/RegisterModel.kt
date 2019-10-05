package net.leonyang.web.huoim.push.bean.api.account

import com.google.gson.annotations.Expose
import org.hibernate.Session

class RegisterModel {

    @Expose
    var account: String? = null

    @Expose
    var password: String? = null

    @Expose
    var name: String? = null

    companion object {
        // 校验
        fun check(model: RegisterModel?): Boolean {
            return null != model
                    && !model.account.isNullOrEmpty()
                    && !model.password.isNullOrEmpty()
                    && !model.name.isNullOrEmpty()
        }
    }

}