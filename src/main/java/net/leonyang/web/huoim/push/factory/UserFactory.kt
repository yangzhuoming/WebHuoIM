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
                return session.save(user) as User
            }
        })
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
