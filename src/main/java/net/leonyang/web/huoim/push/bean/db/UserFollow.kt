package net.leonyang.web.huoim.push.bean.db

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

/**
 * 用户关系的Model，
 * 用于用户直接进行好友关系的实现
 */
@Entity
@Table(name = "TB_USER_FOLLOW")
class UserFollow {

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

    // 定义一个发起人，你关注某人，这里就是你
    // 多对一 -> 你可以关注很多人，你的每一次关注都是一条记录
    // 你可以创建很多个关注的信息，所有是多对一；
    // 这里的多对一是：多个UserFollow 对应 一个 User
    // optional 不可选，必须存储，一条关注记录一定要有一个"你"
    @ManyToOne(optional = false)
    // 定义关联的表字段名为originId，对应的是User.id
    // 定义的是数据库中的存储字段
    @JoinColumn(name = "originId")
    var origin: User? = null

    // 把这个列提取到我们的Model中，不允许为null，不允许更新，插入
    @Column(nullable = false, updatable = false, insertable = false)
    var originId: String? = null

    // 定义关注的目标，你关注的人
    // 也是多对1，你可以被很多人关注，每次一关注都是一条记录
    // 所有就是 多个UserFollow 对应 一个 User 的情况
    @ManyToOne(optional = false)
    // 定义关联的表字段名为targetId，对应的是User.id
    // 定义的是数据库中的存储字段
    @JoinColumn(name = "targetId")
    var target: User? = null

    // 把这个列提取到我们的Model中，不允许为null，不允许更新，插入
    @Column(nullable = false, updatable = false, insertable = false)
    var targetId: String? = null

    // 别名，也就是对target的备注名, 可以为null
    @Column
    var alias: String? = null

    // 定义为创建时间戳，在创建时就已经写入
    @CreationTimestamp
    @Column(nullable = false)
    var createAt = LocalDateTime.now()

    // 定义为更新时间戳，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    var updateAt = LocalDateTime.now()

}