package xyz.relentlesscrew.persistence.DAO;

import org.hibernate.Session;
import xyz.relentlesscrew.persistence.model.Rank;
import xyz.relentlesscrew.persistence.model.Rank_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class RankDAO extends GenericDAOImpl<Rank, Long> {

    public Rank findRankByDiscordRole(Long discordRole) {
        Rank rank = null;
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Rank> query = criteriaBuilder.createQuery(Rank.class);
            Root<Rank> root = query.from(Rank.class);
            query.where(criteriaBuilder.equal(root.get(Rank_.discordRoleId), discordRole));

            rank = session.createQuery(query).uniqueResult();
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + "Caused by: " + e.getCause());
        }
        return rank;
    }
}
