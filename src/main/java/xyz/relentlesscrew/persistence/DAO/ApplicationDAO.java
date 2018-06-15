package xyz.relentlesscrew.persistence.DAO;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import xyz.relentlesscrew.persistence.model.Application;
import xyz.relentlesscrew.persistence.model.Application_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class ApplicationDAO extends GenericDAOImpl<Application, Long> {

    public Application findByDiscordUsername(String discordUsername) {
        Application application = null;
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Application> query = criteriaBuilder.createQuery(Application.class);
            Root<Application> root = query.from(Application.class);
            query.where(criteriaBuilder.equal(root.get(Application_.discordUsername), discordUsername));

            application = session.createQuery(query).uniqueResult();
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + "Caused by: " + e.getCause());
        }
        return application;
    }
}
