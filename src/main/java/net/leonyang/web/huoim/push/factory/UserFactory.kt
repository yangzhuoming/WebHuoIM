package net.leonyang.web.huoim.push.factory

import net.leonyang.web.huoim.push.bean.db.User
import net.leonyang.web.huoim.push.utils.Hib
import org.hibernate.Session
import net.leonyang.web.huoim.push.utils.TextUtil
import java.lang.Exception
import java.util.*
import kotlin.math.log


object UserFactory {

    // 通过Phone找到User
    fun findByPhone(phone: String?): User?{
        return Hib.query(object: Hib.Query<User> {
            override fun query(session: Session): User? =
                session.createQuery("from User where phone=:inPhone")
                    .setParameter("inPhone", phone)
                    .uniqueResult() as User?
        })
    }

    // 通过Name找到User
    fun findByName(name: String?): User? {
        return Hib.query(object: Hib.Query<User> {
            override fun query(session: Session): User? =
                session.createQuery("from User where name=:name")
                    .setParameter("name", name)
                    .uniqueResult() as User?
        })
    }

    // 通过Token字段查询用户信息
    // 只能自己使用，查询的信息是个人信息，非他人信息
    fun findByToken(token: String): User? {
        return Hib.query(object: Hib.Query<User> {
            override fun query(session: Session): User? =
                session.createQuery("from User where token=:token")
                    .setParameter("token", token)
                    .uniqueResult() as User?
        })
    }

    /**
     * 用户注册
     * 注册的操作需要写入数据库，并返回数据库中的User信息
     *
     * @param account  账户
     * @param password 密码
     * @param name     用户名
     * @return User
     */
    fun register(account: String, password: String, name: String): User? {
        var user = createUser(account, encodePassword(password), name)

        if (null != user) {
            user = login(user)
        }
        return user
    }

    /**
     * 使用账户和密码进行登录
     */
    fun login(account: String, password: String): User? {
        var user = Hib.query(object: Hib.Query<User> {
            override fun query(session: Session): User? {
                return session.createQuery("from User where phone=:phone and password=:password")
                    .setParameter("phone", account.trim())
                    .setParameter("password", encodePassword(password))// 把原文进行同样的处理，然后才能匹配
                    .uniqueResult() as User?
            }
        })

        if (null != user) {
            user = login(user)
        }
        return user
    }

    /**
     * 注册部分的新建用户逻辑
     *
     * @param account  手机号
     * @param password 加密后的密码
     * @param name     用户名
     * @return 返回一个用户
     */
    private fun createUser(account: String, password: String, name: String): User? {
        val user = User()
        user.name = name
        user.password = password
        user.phone = account
        return Hib.query(object: Hib.Query<User> {
            override fun query(session: Session): User? {
                session.save(user)
                return user
            }
        })
    }

    fun bindPushId(user: User, pushId: String): User? {
        if(pushId.isNullOrEmpty()) {
            return null
        }

        // 第一步，查询是否有其他账户绑定了这个设备
        // 取消绑定，避免推送混乱
        // 查询的列表不能包括自己
        Hib.queryOnly(object: Hib.QueryOnly {
            override fun query(session: Session) {
                val userList: List<User> = session.createQuery("from User where lower(pushId)=:pushId and id!=:userId")
                    .setParameter("pushId", pushId.toLowerCase())
                    .setParameter("userId", user.id)
                    .list() as List<User>

                for(user in userList) {
                    // 更新为null
                    user.pushId = null
                    session.saveOrUpdate(user)
                }
            }
        })

        if (pushId.equals(user.pushId, true)) {
            // 如果当前需要绑定的设备Id，之前已经绑定过了
            // 那么不需要额外绑定
            return user
        } else {
            // 如果当前账户之前的设备Id，和需要绑定的不同
            // 那么需要单点登录，让之前的设备退出账户，
            // 给之前的设备推送一条退出消息
            if(user.pushId.isNullOrEmpty()) {//这里的判断可能有问题
                // TODO 推送一个退出消息
            }
            // 更新新的设备Id
            user.pushId = pushId
            return Hib.query(object: Hib.Query<User> {
                override fun query(session: Session): User? {
                    session.saveOrUpdate(user)
                    return user
                }
            })
        }
    }

    /**
     * 把一个User进行登录操作
     * 本质上是对Token进行操作
     *
     * @param user User
     * @return User
     */
    private fun login(user: User): User? {
        // 使用一个随机的UUID值充当Token
        //进行一次Base64格式化
        var newToken = TextUtil.encodeBase64(UUID.randomUUID().toString())
        user.token = newToken

        return Hib.query(object: Hib.Query<User> {
            override fun query(session: Session): User? {
                session.saveOrUpdate(user)
                return user
            }
        })
    }

    /**
     * 对密码进行加密操作
     *
     * @param password 原文
     * @return 密文
     */
    private fun encodePassword(password: String?): String {
        var password = password
        // 密码去除首位空格
        password = password!!.trim { it <= ' ' }
        // 进行MD5非对称加密，加盐会更安全，盐也需要存储
        password = TextUtil.getMD5(password)
        // 再进行一次对称的Base64加密，当然可以采取加盐的方案
        return TextUtil.encodeBase64(password!!)
    }

}
