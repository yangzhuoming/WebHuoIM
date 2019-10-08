package net.leonyang.web.huoim.push.bean.api.user

import com.google.gson.annotations.Expose
import net.leonyang.web.huoim.push.bean.db.User


class UpdateInfoModel {

    @Expose
    var name: String? = null
    @Expose
    var portrait: String? = null
    @Expose
    var desc: String? = null
    @Expose
    var sex: Int = 0

    /**
     * 把当前的信息，填充到用户Model中
     * 方便UserModel进行写入
     */

    fun updateToUser(user: User): User {
        if(!name.isNullOrEmpty()) {
            user.name = name
        }
        if(!portrait.isNullOrEmpty()) {
            user.portrait = portrait
        }
        if(!desc.isNullOrEmpty()) {
            user.description = desc
        }
        if(0 != sex) {
            user.sex = sex
        }
        return user
    }

    companion object {
        fun check(model: UpdateInfoModel): Boolean {
            // Model 不允许为null，
            // 并且只需要具有一个及其以上的参数即可
            return !model.name.isNullOrEmpty()
                    || !model.portrait.isNullOrEmpty()
                    || !model.desc.isNullOrEmpty()
                    || 0 != model.sex
        }
    }

}