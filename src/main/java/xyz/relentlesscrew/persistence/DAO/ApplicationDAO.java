package xyz.relentlesscrew.persistence.DAO;

import org.hibernate.Session;
import xyz.relentlesscrew.persistence.model.Application;
import xyz.relentlesscrew.persistence.model.Application_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class ApplicationDAO extends GenericDAOImpl<Application, Long> {

    public Application findByDiscordUsername(String discordUsername) {
        Session session = getSession();

        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Application> query = criteriaBuilder.createQuery(Application.class);
        Root<Application> root = query.from(Application.class);
        query.where(criteriaBuilder.equal(root.get(Application_.discordUsername), discordUsername));

        Application application = session.createQuery(query).uniqueResult();

        session.getTransaction().commit();

        return application;
    }
}
