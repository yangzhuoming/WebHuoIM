package net.leonyang.web.huoim.push.bean.db

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "TB_PUSH_HISTORY")
class PushHistory {

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    var id: String? = null

    // 推送的具体实体存储的都是JSON字符串
    // BLOB 是比TEXT更多的一个大字段类型
    @Lob
    @Column(nullable = false, columnDefinition = "BLOB")
    var entity: String? = null

    // 推送的实体类型
    @Column(nullable = false)
    var entityType: Int? = null

    // 接收者
    // 接收者不允许为空
    // 一个接收者可以接收很多推送消息
    // FetchType.EAGER：加载一条推送消息的时候直接加载用户信息
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "receiverId")// 默认是：receiver_id
    var receiver: User? = null

    @Column(nullable = false, updatable = false, insertable = false)
    var receiverId: String? = null

    // 发送者
    // 发送者可为空，因为可能是系统消息
    // 一个发送者可以发送很多推送消息
    // FetchType.EAGER：加载一条推送消息的时候之间加载用户信息
    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "senderId")
    var sender: User? = null

    @Column(updatable = false, insertable = false)
    var senderId: String? = null

    // 接收者当前状态下的设备推送ID
    // User.pushId 可为null
    @Column
    var receiverPushId: String? = null

    // 定义为更新时间戳，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    var updateAt = LocalDateTime.now()

    // 消息送达的时间，可为空
    @Column
    var arrivalAt: LocalDateTime? = null

}