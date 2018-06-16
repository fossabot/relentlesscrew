package xyz.relentlesscrew.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://docs.jboss.org/hibernate/orm/4.3/manual/en-US/html/ch01.html#tutorial-firstapp-helpers
 */
public class HibernateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateUtil.class);
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .setProperty("hibernate.hikari.dataSource.url", System.getenv("DB_URL"))
                    .setProperty("hibernate.hikari.dataSource.user", System.getenv("DB_USER"))
                    .setProperty("hibernate.hikari.dataSource.password", System.getenv("DB_PASSWORD"))
                    .configure().buildSessionFactory();
        } catch (Throwable throwable) {
            LOGGER.error(throwable.getMessage());
            throw new ExceptionInInitializerError(throwable);
        }
    }

    /**
     *
     * @return session factory
     */
    public static  SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
