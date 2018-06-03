package xyz.relentlesscrew.persistence.DAO;

import org.hibernate.Session;
import xyz.relentlesscrew.persistence.model.Member;
import xyz.relentlesscrew.persistence.model.Member_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class MemberDAO extends GenericDAOImpl<Member, Long> {

    public Member findByDauntlessUsername(String dauntlessUsername) {
        Session session = getSession();

        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Member> query = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = query.from(Member.class);
        query.where(criteriaBuilder.equal(root.get(Member_.dauntlessUsername), dauntlessUsername));

        Member member = session.createQuery(query).uniqueResult();

        session.getTransaction().commit();

        return member;
    }

    public Member findByDiscordId(Long discordId) {
        Session session = getSession();

        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Member> query = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = query.from(Member.class);
        query.where(criteriaBuilder.equal(root.get(Member_.discordId), discordId));

        Member member = session.createQuery(query).uniqueResult();

        session.getTransaction().commit();

        return member;
    }
}
