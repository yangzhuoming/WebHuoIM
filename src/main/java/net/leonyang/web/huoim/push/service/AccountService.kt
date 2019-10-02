package net.leonyang.web.huoim.push.service

import net.leonyang.web.huoim.push.bean.db.User
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

// http://localhost:8080/api/account
@Path("/account")
class AccountService {

    //http://localhost:8080/api/account/login
    @GET
    @Path("/login")
    fun get(): String {
        return "You get the login"
    }

    @POST
    @Path("/login")
    //指定请求与返回的响应体为JSON
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun post(): User {
        return User("LeonYang")
    }

}