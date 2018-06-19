package xyz.relentlesscrew.persistence.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import xyz.relentlesscrew.persistence.model.Rank;
import xyz.relentlesscrew.persistence.model.Rank_;
import xyz.relentlesscrew.util.HibernateUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class RankDAO extends GenericDAOImpl<Rank, Long> {

    /**
     * Find a rank by Discord Role ID
     * @param discordRole id of role
     * @return null if nothing was found
     */
    public Rank findRankByDiscordRole(Long discordRole) {
        Rank rank = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Rank> query = criteriaBuilder.createQuery(Rank.class);
            Root<Rank> root = query.from(Rank.class);
            query.where(criteriaBuilder.equal(root.get(Rank_.discordRoleId), discordRole));

            rank = session.createQuery(query).uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return rank;
    }
}
