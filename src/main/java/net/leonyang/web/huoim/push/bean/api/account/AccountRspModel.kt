package net.leonyang.web.huoim.push.bean.api.account

import com.google.gson.annotations.Expose
import com.sun.org.apache.xpath.internal.operations.Bool
import net.leonyang.web.huoim.push.bean.card.UserCard
import net.leonyang.web.huoim.push.bean.db.User


/**
 * 账户部分返回的Model
 */
class AccountRspModel(user: User, isBind: Boolean) {

    // 用户基本信息
    @Expose
    var user: UserCard? = null
    // 当前登录的账号
    @Expose
    var account: String? = null
    // 当前登录成功后获取的Token,
    // 可以通过Token获取用户的所有信息
    @Expose
    var token: String? = null
    // 标示是否已经绑定到了设备PushId
    @Expose
    var isBind: Boolean = false

    init {
        this.user = UserCard(user)
        this.account = user.phone
        this.token = user.token
        this.isBind = isBind
    }

    constructor(user: User): this(user, false)

}