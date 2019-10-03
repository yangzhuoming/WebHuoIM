package net.leonyang.web.huoim.push.bean.db

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "TB_GROUP_MEMBER")
class GroupMember {

    val PERMISSION_TYPE_NONE = 0 // 默认权限，普通成员
    val PERMISSION_TYPE_ADMIN = 1  // 管理员
    val PERMISSION_TYPE_ADMIN_SU = 100 // 创建者

    val NOTIFY_LEVEL_INVALID = -1 // 默认不接收消息
    val NOTIFY_LEVEL_NONE = 0 // 默认通知级别
    val NOTIFY_LEVEL_CLOSE = 1 // 接收消息不提示

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    var id: String? = null

    //别名
    @Column
    var alias: String? = null

    // 消息通知级别
    @Column(nullable = false)
    var notifyLevel = PERMISSION_TYPE_NONE

    // 成员的权限类型
    @Column(nullable = false)
    var permissionType = PERMISSION_TYPE_NONE

    // 定义为创建时间戳，在创建时就已经写入
    @CreationTimestamp
    @Column(nullable = false)
    var createAt = LocalDateTime.now()

    // 定义为更新时间戳，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    var updateAt = LocalDateTime.now()

    // 成员信息对应的用户信息
    @JoinColumn(name = "userId")
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var user: User? = null

    @Column(nullable = false, updatable = false, insertable = false)
    var userId: String? = null

    // 成员信息对应的群信息
    @JoinColumn(name = "groupId")
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var group: Group? = null

    @Column(nullable = false, updatable = false, insertable = false)
    var groupId: String? = null

}