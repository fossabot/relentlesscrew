package xyz.relentlesscrew.persistence.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.relentlesscrew.util.HibernateUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

    static final Logger LOGGER = LoggerFactory.getLogger(GenericDAO.class);

    private Class<T> clazz;

    @SuppressWarnings("unchecked")
    GenericDAOImpl() {
        this.clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Make a transient object persistent
     * @param transientObject object to make persistent
     * @return true if added
     */
    @Override
    public boolean add(T transientObject) {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction transaction = session.beginTransaction();

            session.save(transientObject);

            transaction.commit();
        } catch (Throwable t) {
            LOGGER.error(t.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Removes a persistent object from the database
     * @param persistentObject object to remove
     * @return true if removed
     */
    @Override
    public boolean remove(T persistentObject) {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction transaction  = session.beginTransaction();

            session.remove(persistentObject);

            transaction.commit();
        } catch (Throwable t) {
            LOGGER.error(t.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Save changes made to a persistent object
     * @param transientObject updated object
     */
    @Override
    public void update(T transientObject) {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction transaction = session.beginTransaction();

            session.update(transientObject);

            transaction.commit();
        } catch (Throwable t) {
            LOGGER.error(t.getMessage());
        }
    }

    /**
     * Fetches the persistent object
     * @param id id of persistent object
     * @return persistent object from the database
     */
    @Override
    public T findById(ID id) {
        T persistentObject = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction transaction = session.beginTransaction();
            persistentObject = session.get(clazz, id);
            transaction.commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
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
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction transaction = session.beginTransaction();
            CriteriaQuery<T> query = session.getCriteriaBuilder().createQuery(clazz);
            query.from(clazz);

            persistentObjects = session.createQuery(query).getResultList();
            transaction.commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return persistentObjects;
    }

    /**
     * Fetches a range of persistent objects
     * @param beginIndex start id
     * @param endIndex last id
     * @return
     */
    @Override
    public List<T> findRange(int beginIndex, int endIndex) {
        List<T> persistentObjects = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction transaction = session.beginTransaction();
            CriteriaQuery<T> query = session.getCriteriaBuilder().createQuery(clazz);
            query.from(clazz);

            persistentObjects = session.createQuery(query)
                    .setFirstResult(beginIndex)
                    .setMaxResults(endIndex - beginIndex)
                    .getResultList();
            transaction.commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return persistentObjects;
    }

    @Override
    public Long countRows() {
        Long rows = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Long> query = builder.createQuery(Long.class);
            query.select(builder.count(query.from(clazz)));

            rows = session.createQuery(query).getSingleResult();
            transaction.commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return rows;
    }

}
