package net.leonyang.web.huoim.push.bean.db

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "TB_APPLY")
class Apply {

    val TYPE_ADD_USER = 1 // 添加好友
    val TYPE_ADD_GROUP = 2 // 加入群

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    var id: String? = null

    // 描述部分，对我们的申请信息做描述
    // eg: 我想加你为好友！！
    @Column(nullable = false)
    var description: String? = null

    // 附件 可为空
    // 可以附带图片地址，或者其他
    @Column(columnDefinition = "TEXT")
    var attach: String? = null

    // 当前申请的类型
    @Column(nullable = false)
    var type: Int? = null

    // 目标Id 不进行强关联，不建立主外键关系
    // type->TYPE_ADD_USER：User.id
    // type->TYPE_ADD_GROUP：Group.id
    @Column(nullable = false)
    var targetId: String? = null

    // 定义为创建时间戳，在创建时就已经写入
    @CreationTimestamp
    @Column(nullable = false)
    var createAt = LocalDateTime.now()

    // 定义为更新时间戳，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    var updateAt = LocalDateTime.now()

    // 申请人 可为空 为系统信息
    // 一个人可以有很多个申请
    @ManyToOne
    @JoinColumn(name = "applicantId")
    var applicant: User? = null

    @Column(updatable = false, insertable = false)
    var applicantId: String? = null

}