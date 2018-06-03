package xyz.relentlesscrew.persistence.DAO;

import org.hibernate.Session;
import xyz.relentlesscrew.persistence.model.Member;
import xyz.relentlesscrew.persistence.model.Member_;
import xyz.relentlesscrew.persistence.model.Rank;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

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

    public List<Member> findByRank(String rankName) {
        Session session = getSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Member> query = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = query.from(Member.class);

        Rank rank = new RankDAO().findByRankName(rankName);

        query.where(criteriaBuilder.equal(root.get(Member_.rank), rank));

        return session.createQuery(query).getResultList();
    }
}
