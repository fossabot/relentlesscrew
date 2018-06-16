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
        Transaction transaction = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();

            session.save(transientObject);

            transaction.commit();
        } catch (Throwable t) {
            LOGGER.error(t.getMessage());
            try {
                if (transaction != null && transaction.isActive()) { transaction.rollback(); }
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
            }
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
        Transaction transaction = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();

            session.remove(persistentObject);

            transaction.commit();
        } catch (Throwable t) {
            LOGGER.error(t.getMessage());
            try {
                if (transaction != null && transaction.isActive()) { transaction.rollback(); }
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
            }
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
        Transaction transaction = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();

            session.update(transientObject);

            transaction.commit();
        } catch (Throwable t) {
            LOGGER.error(t.getMessage());
            try {
                if (transaction != null && transaction.isActive()) { transaction.rollback(); }
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
            }
        }
    }

    /**
     * Fetches the persistent object
     * @param id id of persistent object
     * @return persistent object from the database
     */
    @Override
    public T findById(ID id) {
        Transaction transaction = null;
        T persistentObject = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            persistentObject = session.get(clazz, id);
            transaction.commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            try {
                if (transaction != null && transaction.isActive()) { transaction.rollback(); }
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
            }
        }

        return persistentObject;
    }

    /**
     * Fetches all persistent objects
     * @return all persistent objects from the database
     */
    @Override
    public List<T> findAll() {
        Transaction transaction = null;
        List<T> persistentObjects = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            CriteriaQuery<T> query = session.getCriteriaBuilder().createQuery(clazz);
            query.from(clazz);

            persistentObjects = session.createQuery(query).getResultList();
            transaction.commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            try {
                if (transaction != null && transaction.isActive()) { transaction.rollback(); }
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
            }
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
        Transaction transaction = null;
        List<T> persistentObjects = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            CriteriaQuery<T> query = session.getCriteriaBuilder().createQuery(clazz);
            query.from(clazz);

            persistentObjects = session.createQuery(query)
                    .setFirstResult(beginIndex)
                    .setMaxResults(endIndex - beginIndex)
                    .getResultList();
            transaction.commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            try {
                if (transaction != null && transaction.isActive()) { transaction.rollback(); }
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
            }
        }
        return persistentObjects;
    }

    @Override
    public Long countRows() {
        Transaction transaction = null;
        Long rows = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Long> query = builder.createQuery(Long.class);
            query.select(builder.count(query.from(clazz)));

            rows = session.createQuery(query).getSingleResult();
            transaction.commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            try {
                if (transaction != null && transaction.isActive()) { transaction.rollback(); }
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
            }
        }
        return rows;
    }

}
