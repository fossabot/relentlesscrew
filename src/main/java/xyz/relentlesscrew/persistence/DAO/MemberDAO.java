package xyz.relentlesscrew.persistence.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import xyz.relentlesscrew.persistence.model.Member;
import xyz.relentlesscrew.persistence.model.Member_;
import xyz.relentlesscrew.util.HibernateUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class MemberDAO extends GenericDAOImpl<Member, Long> {

    /**
     * Finds a member with the requested dauntless username
     * @param dauntlessUsername dauntless ingame username
     * @return null if nothing is found, otherwise the persistent object
     */
    public Member findByDauntlessUsername(String dauntlessUsername) {
        Transaction transaction = null;
        Member member = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Member> query = criteriaBuilder.createQuery(Member.class);
            Root<Member> root = query.from(Member.class);
            query.where(criteriaBuilder.equal(root.get(Member_.dauntlessUsername), dauntlessUsername));

            member = session.createQuery(query).uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + "Caused by: " + e.getCause());
            try {
                if (transaction != null && transaction.isActive()) { transaction.rollback(); }
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
            }
        }
        return member;
    }

    /**
     * Finds a member with the requested discord id
     * @param discordId Discord ID (Long)
     * @return null if nothing is found, otherwise the persistent object
     */
    public Member findByDiscordId(Long discordId) {
        Transaction transaction = null;
        Member member = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Member> query = criteriaBuilder.createQuery(Member.class);
            Root<Member> root = query.from(Member.class);
            query.where(criteriaBuilder.equal(root.get(Member_.discordId), discordId));

            member = session.createQuery(query).uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + "Caused by: " + e.getCause());
            try {
                if (transaction != null && transaction.isActive()) { transaction.rollback(); }
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
            }
        }
        return member;
    }
}
