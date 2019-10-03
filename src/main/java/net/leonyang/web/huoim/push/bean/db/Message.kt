package net.leonyang.web.huoim.push.bean.db

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "TB_MESSAGE")
class Message {
    val TYPE_STR = 1 // 字符串类型
    val TYPE_PIC = 2 // 图片类型
    val TYPE_FILE = 3 // 文件类型
    val TYPE_AUDIO = 4 // 语音类型

    // 这是一个主键
    @Id
    @PrimaryKeyJoinColumn
    // 主键生成存储的类型为UUID
    // 这里不自动生成UUID，Id由代码写入，由客户端负责生成
    // 避免复杂的服务器和客户端的映射关系
    //@GeneratedValue(generator = "uuid")
    // 把uuid的生成器定义为uuid2，uuid2是常规的UUID toString
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    // 不允许更改，不允许为null
    @Column(updatable = false, nullable = false)
    var id: String? = null

    // 内容不允许为空，类型为text
    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String? = null

    // 附件
    @Column
    var attach: String? = null

    // 消息类型
    @Column(nullable = false)
    var type: Int = 0

    // 定义为创建时间戳，在创建时就已经写入
    @CreationTimestamp
    @Column(nullable = false)
    var createAt = LocalDateTime.now()

    // 定义为更新时间戳，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    var updateAt = LocalDateTime.now()

    // 发送者 不为空
    // 多个消息对应一个发送者
    @JoinColumn(name = "senderId")
    @ManyToOne(optional = false)
    var sender: User? = null

    // 这个字段仅仅只是为了对应sender的数据库字段senderId
    // 不允许手动的更新或者插入
    @Column(nullable = false, updatable = false, insertable = false)
    var senderId: String? = null

    // 接收者 可为空
    // 多个消息对应一个接收者
    @ManyToOne
    @JoinColumn(name = "receiverId")
    var receiver: User? = null

    @Column(updatable = false, insertable = false)
    var receiverId: String? = null
}