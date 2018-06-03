package xyz.relentlesscrew.persistence.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericDAO.class);

    private Class<T> clazz;

    private SessionFactory sessionFactory = new Configuration()
            .setProperty("hibernate.hikari.dataSource.url", System.getenv("DB_URL"))
            .setProperty("hibernate.hikari.dataSource.user",System.getenv("DB_USER"))
            .setProperty("hibernate.hikari.dataSource.password", System.getenv("DB_PASSWORD"))
            .configure().buildSessionFactory();

    @SuppressWarnings("unchecked")
    public GenericDAOImpl() {
        this.clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Make a transient object persistent
     * @param transientObject
     * @return true if added
     */
    @Override
    public boolean add(T transientObject) {
        try (Session session = getSession()) {
            session.beginTransaction();

            session.save(transientObject);

            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + "Caused by: " + e.getCause());
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
        try (Session session = getSession()) {
            session.beginTransaction();

            session.remove(persistentObject);

            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + "Caused by: " + e.getCause());
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
        try (Session session = getSession()) {
            session.beginTransaction();

            session.update(transientObject);

            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + "Caused by: " + e.getCause());
        }
    }

    /**
     * Fetches the persistent object
     * @param id
     * @return persistent object from the database
     */
    @Override
    public T findById(ID id) {
        Session session = getSession();
        session.beginTransaction();

        T persistentObject = session.get(clazz, id);

        session.getTransaction().commit();
        return persistentObject;
    }

    /**
     * Fetches all persistent objects
     * @return all persistent objects from the database
     */
    @Override
    public List<T> findAll() {
        Session session = getSession();
        session.beginTransaction();

        CriteriaQuery<T> query = session.getCriteriaBuilder().createQuery(clazz);
        query.from(clazz);

        List<T> persistentObjects = session.createQuery(query).getResultList();

        session.getTransaction().commit();

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
        Session session = getSession();
        session.beginTransaction();

        CriteriaQuery<T> query = session.getCriteriaBuilder().createQuery(clazz);
        query.from(clazz);

        List<T> persistentObjects = session.createQuery(query)
                .setFirstResult(beginIndex)
                .setMaxResults(endIndex - beginIndex)
                .getResultList();

        session.getTransaction().commit();

        return persistentObjects;
    }

    @Override
    public Long countRows() {
        Session session = getSession();
        session.beginTransaction();

        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        query.select(builder.count(query.from(clazz)));

        Long rows = session.createQuery(query).getSingleResult();

        session.getTransaction().commit();

        return rows;
    }

    /**
     *
     * @return current session
     */
    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
