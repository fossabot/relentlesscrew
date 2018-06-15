package xyz.relentlesscrew.persistence.DAO;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

    static final Logger LOGGER = LoggerFactory.getLogger(GenericDAO.class);

    private Class<T> clazz;

    SessionFactory sessionFactory = new Configuration()
            .setProperty("hibernate.hikari.dataSource.url", System.getenv("DB_URL"))
            .setProperty("hibernate.hikari.dataSource.user", System.getenv("DB_USER"))
            .setProperty("hibernate.hikari.dataSource.password", System.getenv("DB_PASSWORD"))
            .configure().buildSessionFactory();

    @SuppressWarnings("unchecked")
    GenericDAOImpl() {
        this.clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Make a transient object persistent
     * @param transientObject
     * @return true if added
     */
    @Override
    public boolean add(T transientObject) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.save(transientObject);

            transaction.commit();
        } catch (HibernateException e) {
            LOGGER.error(e.getMessage() + "Caused by: " + e.getCause());
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
        return true;
    }

    /**
     * Removes a persistent object from the database
     * @param persistentObject
     * @return true if removed
     */
    @Override
    public boolean remove(T persistentObject) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.remove(persistentObject);

            transaction.commit();
        } catch (HibernateException e) {
            LOGGER.error(e.getMessage() + "Caused by: " + e.getCause());
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
        return true;
    }

    /**
     * Save changes made to a persistent object
     * @param transientObject
     */
    @Override
    public void update(T transientObject) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.update(transientObject);

            transaction.commit();
        } catch (HibernateException e) {
            LOGGER.error(e.getMessage() + "Caused by: " + e.getCause());
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    /**
     * Fetches the persistent object
     * @param id
     * @return persistent object from the database
     */
    @Override
    public T findById(ID id) {
        T persistentObject = null;
        try (Session session = sessionFactory.openSession()) {
            persistentObject = session.get(clazz, id);
        } catch (HibernateException e) {
            LOGGER.error(e.getMessage() + "Caused by: " + e.getCause());
        }

        return persistentObject;
    }

    /**
     * Fetches all persistent objects
     * @return all persistent objects from the database
     */
    @Override
    public List<T> findAll() {
        List<T> persistentObjects = null;
        try (Session session = sessionFactory.openSession()) {
            CriteriaQuery<T> query = session.getCriteriaBuilder().createQuery(clazz);
            query.from(clazz);

            persistentObjects = session.createQuery(query).getResultList();
        } catch (HibernateException e) {
            LOGGER.error(e.getMessage() + "Caused by: " + e.getCause());
        }
        return persistentObjects;
    }

    /**
     * Fetches a range of persistent objects
     * @param beginIndex
     * @param endIndex
     * @return
     */
    @Override
    public List<T> findRange(int beginIndex, int endIndex) {
        List<T> persistentObjects = null;
        try (Session session = sessionFactory.openSession()) {
            CriteriaQuery<T> query = session.getCriteriaBuilder().createQuery(clazz);
            query.from(clazz);

            persistentObjects = session.createQuery(query)
                    .setFirstResult(beginIndex)
                    .setMaxResults(endIndex - beginIndex)
                    .getResultList();
        } catch (HibernateException e) {
            LOGGER.error(e.getMessage() + "Caused by: " + e.getCause());
        }
        return persistentObjects;
    }

    @Override
    public Long countRows() {
        Long rows = null;
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Long> query = builder.createQuery(Long.class);
            query.select(builder.count(query.from(clazz)));

            rows = session.createQuery(query).getSingleResult();
        } catch (HibernateException e) {
            LOGGER.error(e.getMessage() + "Caused by: " + e.getCause());
        }
        return rows;
    }

}
