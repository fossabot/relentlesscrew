package xyz.relentlesscrew.persistence.DAO;

import org.hibernate.Session;
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
        Member member = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Member> query = criteriaBuilder.createQuery(Member.class);
            Root<Member> root = query.from(Member.class);
            query.where(criteriaBuilder.equal(root.get(Member_.dauntlessUsername), dauntlessUsername));

            member = session.createQuery(query).uniqueResult();
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + "Caused by: " + e.getCause());
        }
        return member;
    }

    /**
     * Finds a member with the requested discord id
     * @param discordId Discord ID (Long)
     * @return null if nothing is found, otherwise the persistent object
     */
    public Member findByDiscordId(Long discordId) {
        Member member = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Member> query = criteriaBuilder.createQuery(Member.class);
            Root<Member> root = query.from(Member.class);
            query.where(criteriaBuilder.equal(root.get(Member_.discordId), discordId));

            member = session.createQuery(query).uniqueResult();
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + "Caused by: " + e.getCause());
        }
        return member;
    }
}
