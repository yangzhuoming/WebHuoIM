package net.leonyang.web.huoim.push.service

import net.leonyang.web.huoim.push.bean.api.base.ResponseModel
import net.leonyang.web.huoim.push.bean.api.user.UpdateInfoModel
import net.leonyang.web.huoim.push.bean.card.UserCard
import net.leonyang.web.huoim.push.bean.db.User
import net.leonyang.web.huoim.push.factory.UserFactory
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

//127.0.0.1/api/user
@Path("/user")
class UserService: BaseService() {
    // 用户信息修改接口
    // 返回自己的个人信息
    @PUT//put主要用于修改，post用于新建
    //@Path("") 不需要写，就是当前目录
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun update(model: UpdateInfoModel): ResponseModel<UserCard> {
        if(!UpdateInfoModel.check(model)) {
            return ResponseModel.buildParameterError()
        }
        // 更新用户 信息
        val self = getSelf().apply {
            model.updateToUser(this)
            UserFactory.update(this)
        }
        // 构架自己的用户信息
        return ResponseModel.buildOk(UserCard(self, true))
    }
}