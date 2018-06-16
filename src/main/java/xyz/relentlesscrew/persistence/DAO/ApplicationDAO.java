package xyz.relentlesscrew.persistence.DAO;

import org.hibernate.Session;
import xyz.relentlesscrew.persistence.model.Application;
import xyz.relentlesscrew.persistence.model.Application_;
import xyz.relentlesscrew.util.HibernateUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class ApplicationDAO extends GenericDAOImpl<Application, Long> {

    /**
     * Find an application by discord username
     * @param discordUsername discord username to find
     * @return null if nothing is found, otherwise the persistent object
     */
    public Application findByDiscordUsername(String discordUsername) {
        Application application = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
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
