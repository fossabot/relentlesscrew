package xyz.relentlesscrew.persistence.DAO;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import xyz.relentlesscrew.persistence.model.Member;
import xyz.relentlesscrew.persistence.model.Member_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class MemberDAO extends GenericDAOImpl<Member, Long> {

    public Member findByDauntlessUsername(String dauntlessUsername) {
        Member member = null;
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Member> query = criteriaBuilder.createQuery(Member.class);
            Root<Member> root = query.from(Member.class);
            query.where(criteriaBuilder.equal(root.get(Member_.dauntlessUsername), dauntlessUsername));

            member = session.createQuery(query).uniqueResult();
        } catch (HibernateException e) {
            LOGGER.error(e.getMessage() + "Caused by: " + e.getCause());
        }
        return member;
    }

    public Member findByDiscordId(Long discordId) {
        Member member = null;
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Member> query = criteriaBuilder.createQuery(Member.class);
            Root<Member> root = query.from(Member.class);
            query.where(criteriaBuilder.equal(root.get(Member_.discordId), discordId));

            member = session.createQuery(query).uniqueResult();
        } catch (HibernateException e) {
            LOGGER.error(e.getMessage() + "Caused by: " + e.getCause());
        }
        return member;
    }
}
