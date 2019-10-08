package net.leonyang.web.huoim.push.bean.card

import java.time.LocalDateTime
import com.google.gson.annotations.Expose
import net.leonyang.web.huoim.push.bean.db.User


class UserCard(user: User, isFollow: Boolean) {

    @Expose
    var id: String? = null
    @Expose
    var name: String? = null
    @Expose
    var phone: String? = null
    @Expose
    var portrait: String? = null
    @Expose
    var desc: String? = null
    @Expose
    var sex = 0

    // 用户关注人的数量
    @Expose
    var follows: Int = 0

    // 用户粉丝的数量
    @Expose
    var following: Int = 0

    // 我与当前User的关系状态，是否已经关注了这个人
    @Expose
    var isFollow: Boolean = false

    // 用户信息最后的更新时间
    @Expose
    var modifyAt: LocalDateTime? = null

    init {
        id = user.id
        name = user.name
        phone = user.phone
        portrait = user.portrait
        desc = user.description
        sex = user.sex
        modifyAt = user.updateAt
        this.isFollow = isFollow
        // TODO 得到关注人和粉丝的数量
        // user.getFollowers().size()
        // 懒加载会报错，因为没有Session

    }

    constructor(user: User): this(user, false)

}