package net.leonyang.web.huoim.push.utils

import org.hibernate.SessionFactory
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistry
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.mapping.MetadataSource
import java.lang.Exception

object Hib {

    var sessionFactory: SessionFactory? = null

    var session = sessionFactory?.currentSession

    init {
        // 从hibernate.cfg.xml文件初始化
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

}