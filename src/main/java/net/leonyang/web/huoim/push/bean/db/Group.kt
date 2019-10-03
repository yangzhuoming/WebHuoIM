package net.leonyang.web.huoim.push.bean.db

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "TB_GROUP")
class Group {

    // 这是一个主键
    @Id
    @PrimaryKeyJoinColumn
    // 主键生成存储的类型为UUID，自动生成UUID
    @GeneratedValue(generator = "uuid")
    // 把uuid的生成器定义为uuid2，uuid2是常规的UUID toString
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    // 不允许更改，不允许为null
    @Column(updatable = false, nullable = false)
    var id: String? = null

    // 群名称
    @Column(nullable = false)
    var name: String? = null

    // 群描述
    @Column(nullable = false)
    var description: String? = null

    @Column(nullable = false)
    var picture: String? = null

    // 定义为创建时间戳，在创建时就已经写入
    @CreationTimestamp
    @Column(nullable = false)
    var createAt = LocalDateTime.now()

    // 定义为更新时间戳，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    var updateAt = LocalDateTime.now()

    // 群的创建者
    // optional: 可选为false，必须有一个创建者
    // fetch: 加载方式FetchType.EAGER，急加载，
    // 意味着加载群的信息的时候就必须加载owner的信息
    // cascade：联级级别为ALL，所有的更改（更新，删除等）都将进行关系更新
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "ownerId")
    var owner: User? = null

    @Column(nullable = false, updatable = false, insertable = false)
    var ownerId: String? = null
}