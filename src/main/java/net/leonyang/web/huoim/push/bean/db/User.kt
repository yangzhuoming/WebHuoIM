package net.leonyang.web.huoim.push.bean.db

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*
import javax.persistence.GeneratedValue
import javax.persistence.PrimaryKeyJoinColumn
import java.time.LocalDateTime
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.CreationTimestamp


/**
 * 用户的Model，对应数据库
 */
@Entity
@Table(name = "TB_USER")
class User{

    // 这是一个主键
    @Id
    @PrimaryKeyJoinColumn
    // 主键生成存储的类型为UUID
    @GeneratedValue(generator = "uuid")
    // 把uuid的生成器定义为uuid2，uuid2是常规的UUID toString
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    // 不允许更改，不允许为null
    @Column(updatable = false, nullable = false)
    var id: String? = null

    // 用户名必须唯一
    @Column(nullable = false, length = 128, unique = true)
    var name: String? = null

    // 电话必须唯一
    @Column(nullable = false, length = 62, unique = true)
    var phone: String? = null

    @Column(nullable = false)
    var password: String? = null

    // 头像允许为null
    @Column
    var portrait: String? = null

    @Column
    var description: String? = null

    // 性别有初始值，所有不为空
    @Column(nullable = false)
    var sex = 0

    // token 可以拉取用户信息，所有token必须唯一
    @Column(unique = true)
    var token: String? = null

    // 用于推送的设备ID
    @Column
    var pushId: String? = null

    // 定义为创建时间戳，在创建时就已经写入
    @CreationTimestamp
    @Column(nullable = false)
    var createAt = LocalDateTime.now()

    // 定义为更新时间戳，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    var updateAt = LocalDateTime.now()

    // 最后一次收到消息的时间
    @Column
    var lastReceivedAt = LocalDateTime.now()
}