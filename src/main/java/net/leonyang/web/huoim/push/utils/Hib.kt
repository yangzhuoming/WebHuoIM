package net.leonyang.web.huoim.push.utils

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import java.lang.Exception
import java.lang.RuntimeException

class Hib {

    companion object {
        var sessionFactory: SessionFactory? = null

        var session: Session? = null
            get() = sessionFactory?.currentSession

        init {
            // 从hibernate.cfg.xml文件初始化d
            val registry = StandardServiceRegistryBuilder()
                .configure()
                .build()
            try {
                sessionFactory = MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory()
            } catch (e: Exception) {
                e.printStackTrace()
                StandardServiceRegistryBuilder.destroy(registry)
            }

        }

        fun closeFactory() {
            if (null != sessionFactory) {
                sessionFactory!!.close()
            }
        }



        //简化Session事物操的一个工具方法
        fun queryOnly(query: QueryOnly) {
            // 重开一个Session
            val session = sessionFactory!!.openSession()

            // 开启事务
            val transaction = session.beginTransaction()

            try {
                // 调用传递进来的接口，
                // 并调用接口的方法把Session传递进去
                query.query(session)
                // 提交
                transaction.commit()
            } catch (e: Exception) {
                e.printStackTrace()
                //回滚
                try {
                    transaction.rollback()
                } catch (e: RuntimeException) {
                    e.printStackTrace()
                }
            } finally {
                // 无论成功失败，都需要关闭Session
                session.close();
            }
        }

        // 简化Session操作的工具方法，
        // 具有一个返回值
        fun <T> query(query: Query<T>): T? {
            // 重开一个Session
            val session = sessionFactory!!.openSession()
            // 开启事务
            val transaction = session.beginTransaction()

            var t: T? = null
            try {
                // 调用传递进来的接口，
                // 并调用接口的方法把Session传递进去
                t = query.query(session)
                // 提交
                transaction.commit()
            } catch (e: Exception) {
                e.printStackTrace()
                //回滚
                try {
                    transaction.rollback()
                } catch (e: RuntimeException) {
                    e.printStackTrace()
                }
            } finally {
                // 无论成功失败，都需要关闭Session
                session.close();
            }
            return t
        }

    }

    interface QueryOnly {
        fun query(session: Session)
    }

    interface Query<T> {
        fun query(session: Session): T?
    }



}